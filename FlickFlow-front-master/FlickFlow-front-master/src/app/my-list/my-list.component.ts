import { Component, OnInit } from '@angular/core';
import { MovieService } from '../services/movie.service';
import { HistoryService } from '../services/history.service';
import {DatePipe, NgClass, NgForOf, NgOptimizedImage, NgStyle, SlicePipe} from "@angular/common";
import {AuthService} from "../services/auth.service";
import {UserService} from "../services/user.service";
import {NavbarComponent} from "../shared/navbar/navbar.component";

@Component({
  selector: 'app-my-list',
  standalone: true,
  imports: [
    NgClass,
    NgOptimizedImage,
    NgForOf,
    DatePipe,
    NgStyle,
    SlicePipe,
    NavbarComponent
  ],
  templateUrl: './my-list.component.html',
  styleUrl: './my-list.component.css'
})
export class MyListComponent implements OnInit {
  movies: any[] = [];
  userId: string | null = null;
  currentMovie: any = { backdropPath: '', title: '', overview: '' };
  currentMovieIndex: number = 0;

  constructor(
    private historyService: HistoryService,
    private movieService: MovieService,
    private authService: UserService) { }

  ngOnInit(): void {
    this.userId = this.authService.getUserId();
    if (this.userId) {
      this.loadHistory();
    } else {
      console.error('User ID not found');
    }setInterval(() => {
      this.cycleMovie();
    }, 4000); // Change every 4 seconds
  }



  loadHistory(): void {
    if (this.userId) {
      this.historyService.getUserHistory(this.userId).subscribe(history => {
        history.forEach(historyItem => {
          this.movieService.getMovieDetails(historyItem.movieId).subscribe(movie => {
            this.movies.push({
              title: movie.title,
              posterPath: 'https://image.tmdb.org/t/p/original/' + movie.posterPath,
              overview: movie.overview,
              releaseDate: movie.releaseDate,
              watchedAt: historyItem.watchedAt
            });

            // Set the first movie as the current movie
            if (this.movies.length === 1) {
              this.currentMovie = this.movies[0];
            }
          });
        });
      });
    }
  }

  cycleMovie(): void {
    if (this.movies.length > 0) {
      this.currentMovieIndex = (this.currentMovieIndex + 1) % this.movies.length;
      this.currentMovie = this.movies[this.currentMovieIndex];
    }
  }

}
