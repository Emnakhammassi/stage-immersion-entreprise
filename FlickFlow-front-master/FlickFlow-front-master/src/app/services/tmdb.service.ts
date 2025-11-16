import {computed, inject, Injectable, Signal, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {Cast, Moviemodel, MovieApiResponse, Trailer} from "./model/moviemodel";
import { State } from "./model/state";
import { environment } from "../../environments/environment";
import {Genre, GenresResponse} from "./model/genre";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import {MoreInfosComponent} from "../home/more-infos/more-infos.component";
import {catchError, map, Observable, of} from "rxjs";
// import { MoreInfosComponent } from "../home/more-infos/more-infos.component";

@Injectable({
  providedIn: 'root'
})
export class TmdbService {
  http = inject(HttpClient);
  modalService = inject(NgbModal);
  private genreList: Genre[] = [];
  private genreMap: { [id: number]: string } = {};

  baseURL = 'https://api.themoviedb.org';

  private fetchTrendMovie$: WritableSignal<State<MovieApiResponse, HttpErrorResponse>>
    = signal(State.Builder<MovieApiResponse, HttpErrorResponse>().forInit().build());
  fetchTrendMovie = computed(() => this.fetchTrendMovie$());

  private genres$: WritableSignal<State<GenresResponse, HttpErrorResponse>>
    = signal(State.Builder<GenresResponse, HttpErrorResponse>().forInit().build());
  genres = computed(() => this.genres$());

  private moviesByGenre$: WritableSignal<State<MovieApiResponse, HttpErrorResponse>>
    = signal(State.Builder<MovieApiResponse, HttpErrorResponse>().forInit().build());
  moviesByGenre = computed(() => this.moviesByGenre$());

  private movieById$: WritableSignal<State<Moviemodel, HttpErrorResponse>>
    = signal(State.Builder<Moviemodel, HttpErrorResponse>().forInit().build());
  movieById = computed(() => this.movieById$());

  private search$: WritableSignal<State<MovieApiResponse, HttpErrorResponse>>
    = signal(State.Builder<MovieApiResponse, HttpErrorResponse>().forInit().build());
  search = computed(() => this.search$());

  getTrends(): void {
    this.http.get<MovieApiResponse>(
      `${this.baseURL}/3/trending/movie/day?api_key=${environment.TMDB_API_KEY}`)
      .subscribe({
        next: tmdbResponse =>
          this.fetchTrendMovie$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forSuccess(tmdbResponse).build()),
        error: err => {
          this.fetchTrendMovie$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forError(err).build())
        }
      });
  }

  getAllGenres(): void {
    this.http.get<GenresResponse>(
      `${this.baseURL}/3/genre/movie/list?api_key=${environment.TMDB_API_KEY}`)
      .subscribe({
        next: genresResponse =>
          this.genres$
            .set(State.Builder<GenresResponse, HttpErrorResponse>()
              .forSuccess(genresResponse).build()),
        error: err => {
          this.genres$
            .set(State.Builder<GenresResponse, HttpErrorResponse>()
              .forError(err).build())
        }
      });
  }

  getImageURL(id: string, size: 'original' | 'w500' | 'w200'): string {
    return `https://image.tmdb.org/t/p/${size}/${id}`;
  }

  getMoviesByGenre(genreId: number): void {
    let queryParam = new HttpParams();
    queryParam = queryParam.set("language", "en-US");
    queryParam = queryParam.set("with_genres", genreId);
    this.http.get<MovieApiResponse>(
      `${this.baseURL}/3/discover/movie?api_key=${environment.TMDB_API_KEY}`, {params: queryParam})
      .subscribe({
        next: moviesByGenreResponse => {
          moviesByGenreResponse.genreId = genreId;
          this.moviesByGenre$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forSuccess(moviesByGenreResponse).build())
        },
        error: err => {
          this.moviesByGenre$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forError(err).build())
        }
      });
  }

  getMovieById(id: number): void {
    this.http.get<Moviemodel>(
      `${this.baseURL}/3/movie/${id}?api_key=${environment.TMDB_API_KEY}`)
      .subscribe({
        next: movieResponse => {
          this.movieById$
            .set(State.Builder<Moviemodel, HttpErrorResponse>()
              .forSuccess(movieResponse).build())
        },
        error: err => {
          this.movieById$
            .set(State.Builder<Moviemodel, HttpErrorResponse>()
              .forError(err).build())
        }
      });
  }

  constructor() {
    this.loadGenres();
  }


  clearGetMovieById() {
    this.movieById$.set(State.Builder<Moviemodel, HttpErrorResponse>().forInit().build());
  }

  getHeaders(): HttpHeaders {
    return new HttpHeaders().set('Authorization', `Bearer ${environment.TMDB_API_KEY}`);
  }
  openMoreInfos(movieId: number): void {
    let moreInfoModal = this.modalService.open(MoreInfosComponent);
    moreInfoModal.componentInstance.movieId = movieId;
  }

  searchByTerm(term: string): void {
    let queryParam = new HttpParams();
    queryParam = queryParam.set("language", "en-US");
    queryParam = queryParam.set("query", term);
    this.http.get<MovieApiResponse>(
      `${this.baseURL}/3/search/movie?api_key=${environment.TMDB_API_KEY}`, {params: queryParam})
      .subscribe({
        next: searchByTerm => {
          this.search$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forSuccess(searchByTerm).build())
        },
        error: err => {
          this.search$
            .set(State.Builder<MovieApiResponse, HttpErrorResponse>()
              .forError(err).build())
        }
      });
  }














  private loadGenres(): void {
    this.http.get(`${this.baseURL}/3/genre/movie/list?api_key=${environment.TMDB_API_KEY}`)
      .subscribe((response: any) => {
        response.genres.forEach((genre: { id: number, name: string }) => {
          this.genreMap[genre.id] = genre.name;
        });
      });
  }


  getMovieTrailer(movieId: number): Observable<string> {
    return this.http.get(
      `${this.baseURL}/3/movie/${movieId}/videos?api_key=${environment.TMDB_API_KEY}`
    ).pipe(
      map((response: any) => {
        const trailer = response.results.find(
          (video: { type: string }) => video.type === 'Trailer'
        );
        return trailer ? `https://www.youtube.com/embed/${trailer.key}` : '';
      })
    );
  }
  getMovieId(id: number): Observable<Moviemodel> {
    return this.http.get<Moviemodel>(
      `${this.baseURL}/3/movie/${id}?api_key=${environment.TMDB_API_KEY}&append_to_response=videos`
    ).pipe(
      map((response: any) => {
        const movie: Moviemodel = response;
        movie.genre_ids = response.genres.map((genre: { name: string }) => genre.name);
        return movie;
      })
    );
  }

  getCast(movieId: number): Observable<any> {
    return this.http.get(`${this.baseURL}/3/movie/${movieId}/credits?api_key=${environment.TMDB_API_KEY}`);
  }

  getRelatedMovies(movieId: number): Observable<any> {
    return this.http.get(`${this.baseURL}/3/movie/${movieId}/similar?api_key=${environment.TMDB_API_KEY}`);
  }











  private movieCast$: WritableSignal<State<Cast[], HttpErrorResponse>> = signal(State.Builder<Cast[], HttpErrorResponse>().forInit().build());
  movieCast = computed(() => this.movieCast$());

  // Signal for storing the state of the fetched trailers
  private movieTrailers$: WritableSignal<State<Trailer[], HttpErrorResponse>> = signal(State.Builder<Trailer[], HttpErrorResponse>().forInit().build());
  movieTrailers = computed(() => this.movieTrailers$());


  //
  // // Fetch cast information by movie ID
  // getMovieCast(id: number): void {
  //   this.http.get<Cast[]>(`${this.baseURL}/movies/${id}/cast`)
  //     .subscribe({
  //       next: castResponse => {
  //         this.movieCast$.set(State.Builder<Cast[], HttpErrorResponse>().forSuccess(castResponse).build());
  //       },
  //       error: err => {
  //         this.movieCast$.set(State.Builder<Cast[], HttpErrorResponse>().forError(err).build());
  //       }
  //     });
  // }

  // // Fetch trailers by movie ID
  // getMovieTrailers(id: number): void {
  //   this.http.get<Trailer[]>(`${this.baseURL}/movies/${id}/trailers`)
  //     .subscribe({
  //       next: trailersResponse => {
  //         this.movieTrailers$.set(State.Builder<Trailer[], HttpErrorResponse>().forSuccess(trailersResponse).build());
  //       },
  //       error: err => {
  //         this.movieTrailers$.set(State.Builder<Trailer[], HttpErrorResponse>().forError(err).build());
  //       }
  //     });
  // }

  castByMovieId(movieId: number): Signal<State<Cast[], any>> {
    const state = signal<State<Cast[], any>>(new State<Cast[], any>('INIT'));

    this.http.get<{ cast: Cast[] }>(`${this.baseURL}/movie/${movieId}/credits?api_key=${environment.TMDB_API_KEY}`)
      .pipe(
        map(response => {
          return State.Builder<Cast[], any>().forSuccess(response.cast).build();
        }),
        catchError(error => {
          return of(State.Builder<Cast[], any>().forError(error).build());
        })
      ).subscribe(result => state.set(result));

    return state;
  }

  trailersByMovieId(movieId: number): Signal<State<Trailer[], any>> {
    const state = signal<State<Trailer[], any>>(new State<Trailer[], any>('INIT'));

    this.http.get<{ results: Trailer[] }>(`${this.baseURL}/movie/${movieId}/videos?api_key=${environment.TMDB_API_KEY}`)
      .pipe(
        map(response => {
          return State.Builder<Trailer[], any>().forSuccess(response.results).build();
        }),
        catchError(error => {
          return of(State.Builder<Trailer[], any>().forError(error).build());
        })
      ).subscribe(result => state.set(result));

    return state;
  }


  getMovieDetails(movieId: number): Observable<Moviemodel> {
    return this.http.get<Moviemodel>(`${this.baseURL}/genre/movie/list?api_key=${environment.TMDB_API_KEY}`);
  }

  getMovieCast(movieId: number): Observable<Cast[]> {
    return this.http.get<{ cast: Cast[] }>(`${this.baseURL}/movie/${movieId}/credits?api_key=${environment.TMDB_API_KEY}`)
      .pipe(map(response => response.cast));
  }
  getMovieTrailers(movieId: number): Observable<Trailer[]> {
    return this.http.get<{ results: Trailer[] }>(`${this.baseURL}/movie/${movieId}/videos?api_key=${environment.TMDB_API_KEY}`)
      .pipe(map(response => response.results));
  }

  getGenres(): Observable<GenresResponse> {
    return this.http.get<GenresResponse>(`${this.baseURL}/genre/movie/list?api_key=${environment.TMDB_API_KEY}`);
  }

  getGenreName(genreId: number): string {
    const genre = this.genreList.find(g => g.id === genreId);
    return genre ? genre.name : '';
  }

}
