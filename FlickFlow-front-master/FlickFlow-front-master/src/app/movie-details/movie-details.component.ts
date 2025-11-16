import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  CUSTOM_ELEMENTS_SCHEMA,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import {MovieService} from '../services/movie.service';
import {MovieDetails} from '../services/model/movie-details';
import {Cast} from '../services/model/credits';
import {DomSanitizer, SafeHtml, SafeResourceUrl} from '@angular/platform-browser';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {IgxProgressBarModule} from "igniteui-angular";
import {Movie} from "../services/model/movie";
import {NavbarComponent} from "../shared/navbar/navbar.component";
import {HistoryService} from "../services/history.service";
import {UserService} from "../services/user.service";
import {RatingComponent} from "./rating/rating.component";


@Component({
  selector: 'app-movie-details',
  standalone: true,
  imports: [CommonModule, IgxProgressBarModule, RouterLink, NavbarComponent, RatingComponent,],
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.scss'],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  animations: [
    trigger('backdropToVideo', [
      state('backdrop', style({
        opacity: 1,
        zIndex: 10
      })),
      state('video', style({
        opacity: 1,
        zIndex: -1
      })),
      transition('backdrop => video', [
        style({ opacity: 1, zIndex: 10 }), // start state
        animate('1s ease-out', style({ opacity: 1 })), // end state
      ]),
      transition('video => backdrop', [
        style({ opacity: 0, zIndex: -1 }), // start state
        animate('1s ease-out', style({ opacity: 1, zIndex: 10 })), // end state
      ]),
    ])
  ]
})
export class MovieDetailsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('youtubePlayer') youtubePlayer?: ElementRef<HTMLIFrameElement>;
  getMovieDetailResult?: MovieDetails;
  getMovieCastResult?: Cast[];
  getMovieVideoResult?: string;
  displayedCast?: Cast[];
  castLimit = 7;
  movieStreamHtml?: SafeHtml;
  videoUrl?: SafeResourceUrl;
  isMuted = true;
  animate: boolean = false;
  animationState = 'backdrop';
  private videoCheckInterval?: any;
  previousVoteAverage?: number;
  displayVoteAverage?: number;
  circleColor: string = 'green';
  circleBackgroundColor: string = 'green';
  currentValue: number = 0;
  movieGenreResults: Movie[] = [];
  movieId?: number; // Declare movieId property
  userId?: string | null;
  ratingValue?:number;



  constructor(
    private moviesService: MovieService,
    private HistoryService: HistoryService,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef,
    private userService: UserService

  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        const id = +idParam; // Convert to number
        if (!isNaN(id)) {
          this.movieId = +idParam; // Assign movieId from route parameters
          this.getMovie(id);
          this.getMovieCast(id);
          this.getMovieVideo(id);
          this.getMovieStream(id);
          this.loadMoviesByGenres();
          this.userId = this.userService.getUserId();

          // Initial state
          this.animationState = 'backdrop';
          this.cdr.detectChanges();

          // Delay changing the animation state
          setTimeout(() => {
            this.animationState = 'video';
            this.cdr.detectChanges();
            this.muteVideo();
          }, 30000); // 4 seconds delay
        } else {
          console.error('Invalid movie ID:', idParam);
        }
      } else {
        console.error('Movie ID not found in route parameters');
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.youtubePlayer) {
      this.startVideoPolling();
    }
  }

  ngOnDestroy(): void {
    if (this.videoCheckInterval) {
      clearInterval(this.videoCheckInterval);
    }
  }

  loadMoviesByGenres(): void {
    if (this.getMovieDetailResult?.genres?.length) {
      const genreIds = this.getMovieDetailResult.genres.map(g => g.id);
      this.moviesService.fetchMoviesByGenre(genreIds).subscribe({
        next: (movies) => {
          this.movieGenreResults = movies;
        },
        error: (error) => {
          console.error('Error loading movies by genres:', error);
        }
      });
    }
  }

  startVideoPolling(): void {
    this.videoCheckInterval = setInterval(() => {
      const iframe = this.youtubePlayer?.nativeElement;
      if (iframe) {
        const src = iframe.src;
        if (src && src.includes('youtube.com') && !this.isVideoPlaying(iframe)) {
          this.onVideoEnd();
        }
      }
    }, 1000);
  }

  isVideoPlaying(iframe: HTMLIFrameElement): boolean {
    // Placeholder for video playing detection logic
    return false;
  }

  getMovie(id: number): void {
    this.moviesService.getMovieDetails(id).subscribe({
      next: (result: MovieDetails) => {
        this.getMovieDetailResult = result;
        this.previousVoteAverage = result.voteAverage;
        this.setCircleColor(this.previousVoteAverage);

        // Load recommendations by genre after movie details are loaded
        this.loadMoviesByGenres();
      },
      error: (error) => {
        console.error('Error fetching movie details:', error);
      }
    });
  }

  get circleDashArray(): string {
    const radius = 45; // Same as the circle's radius
    const circumference = 2 * Math.PI * radius;
    return `${circumference}`;
  }

  get circleDashOffset(): string {
    const radius = 45; // Same as the circle's radius
    const circumference = 2 * Math.PI * radius;
    const offset = circumference - (this.currentValue / 100) * circumference;
    return `${offset}`;
  }

  setCircleColor(previousVoteAverage: number): void {
    this.displayVoteAverage = Math.round(previousVoteAverage);
    this.currentValue = this.displayVoteAverage * 10;
    this.circleColor = this.getColorForPercentage(this.currentValue);
    this.circleBackgroundColor = this.getBackgroundColorForPercentage(this.currentValue);
    this.cdr.detectChanges(); // Ensure changes are detected
  }


  getColorForPercentage(value: number): string {
    if (value < 40) {
      return 'red';
    } else if (value < 70) {
      return 'yellow';
    } else {
      return 'green';
    }
  }

  getBackgroundColorForPercentage(value: number): string {
    // You can set different colors with low opacity based on the percentage
    if (value < 40) {
      return 'rgba(255, 0, 0, 0.2)'; // Red with 30% opacity
    } else if (value < 70) {
      return 'rgba(255, 255, 0, 0.2)'; // Yellow with 30% opacity
    } else {
      return 'rgba(0, 255, 0, 0.2)'; // Green with 30% opacity
    }
  }


  getMovieCast(id: number): void {
    this.moviesService.getMovieCast(id).subscribe({
      next: (result: Cast[]) => {
        console.log('Raw API response:', result);
        if (Array.isArray(result)) {
          if (result.length > 0) {
            this.getMovieCastResult = result;
            this.displayedCast = result.slice(0, this.castLimit);
            console.log('Movie cast fetched successfully:', this.displayedCast);
          } else {
            console.error('No cast information found');
          }
        } else {
          console.error('Response is not an array');
        }
      },
      error: (error) => {
        console.error('Error fetching movie cast:', error);
      }
    });
  }

  getMovieStream(id: number): void {
    this.moviesService.streamMovie(id).subscribe({
      next: (result: string) => {
        this.movieStreamHtml = this.sanitizer.bypassSecurityTrustHtml(result);
        console.log('Stream data fetched successfully:', result);
        this.cdr.detectChanges();
        this.applyDynamicStyles();
      },
      error: (error) => {
        console.error('Error fetching stream data:', error);
      }
    });
  }

  getMovieVideo(id: number): void {
    this.moviesService.getMovieVideo(id).subscribe({
      next: (result) => {
        if (result && result.length > 0) {
          this.getMovieVideoResult = result[0].key;
          this.setVideoUrl();
        } else {
          console.error('No video found');
        }
      },
      error: (error) => {
        console.error('Error fetching movie video:', error);
      }
    });
  }

  setVideoUrl(): void {
    if (this.getMovieVideoResult) {
      const baseUrl = `https://www.youtube.com/embed/${this.getMovieVideoResult}`;
      const params = `?autoplay=1&mute=1&controls=0&enablejsapi=1&modestbranding=1`;
      this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(baseUrl + params);
    }
  }

  toggleMute(): void {
    this.isMuted = !this.isMuted;
    const action = this.isMuted ? 'mute' : 'unMute';
    this.sendMessageToIframe(action);

    // Trigger the animation
    this.animate = true;

    // Reset animation state after animation duration
    setTimeout(() => {
      this.animate = false;
    }, 1250); // Duration of the animation
  }

  muteVideo(): void {
    if (this.youtubePlayer) {
      this.sendMessageToIframe('mute');
    }
  }

  onVideoEnd(): void {
    this.animationState = 'backdrop';
    this.cdr.detectChanges();
    if (this.videoCheckInterval) {
      clearInterval(this.videoCheckInterval);
    }
  }

  sendMessageToIframe(action: string): void {
    const iframe = this.youtubePlayer?.nativeElement;
    if (iframe) {
      const iframeWindow = iframe.contentWindow;
      if (iframeWindow) {
        iframeWindow.postMessage(`{"event":"command","func":"${action}","args":""}`, '*');
      }
    }
  }

  applyDynamicStyles(): void {
    const movieStreamDiv = document.querySelector('.movie_stream');
    if (movieStreamDiv) {
      const iframe = movieStreamDiv.querySelector('#player_iframe') as HTMLIFrameElement;
      if (iframe) {
        iframe.style.height = '60rem';
        iframe.style.width = '95%';
      }
    }
  }

  protected readonly length = length;

  onStream(): void {
    if (this.userId && this.movieId) {
      this.HistoryService.saveToHistory(this.userId, this.movieId).subscribe({
        next: () => console.log('Movie saved to history'),
        error: (error) => {
          console.error('Error saving movie to history:', error);
        }
      });
    } else {
      console.error('User ID or Movie ID is not available');
    }
  }
}
