import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Movie, Movies, Dates, OriginalLanguage } from './model/movie';
import {MovieDetails} from "./model/movie-details"; // Adjust the import path accordingly

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private apiUrl = 'http://localhost:8083/api/movie'; // Base URL for the backend

  constructor(private http: HttpClient) { }

  // Fetch movies by title
  fetchMovieByTitle(title: string): Observable<any> {
    console.log('Fetching movie with title:', title);
    return this.http.get<any>(`${this.apiUrl}/search?title=${title}`).pipe(
      catchError(this.handleError)
    );
  }

  // Get movie details by ID
  getMovieDetails(id: number): Observable<MovieDetails> {
    return this.http.get<MovieDetails>(`${this.apiUrl}/movie/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Get movie cast by movie ID
  getMovieCast(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/cast/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Fetch popular movies
  fetchPopularMovies(): Observable<Movies> {
    return this.http.get<Movies>(`${this.apiUrl}/home`).pipe(
      catchError(this.handleError)
    );
  }

  // Fetch movies by genre
  fetchMoviesByGenre(genreIds: number[]): Observable<Movie[]> {
    const ids = genreIds.join(',');
    return this.http.get<Movie[]>(`${this.apiUrl}/genre?ids=${ids}`).pipe(
      catchError(this.handleError)
    );
  }


  // Fetch trending movies
  fetchTrendingMovies(): Observable<Movies> {
    return this.http.get<Movies>(`${this.apiUrl}/trending`).pipe(
      catchError(this.handleError)
    );
  }

  // Stream movie content by ID
  streamMovie(id: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/stream/movie/${id}`, { responseType: 'text' as 'json' }).pipe(
      catchError(this.handleError)
    );
  }

  // Get movie video by ID
  getMovieVideo(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/movievideo/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Fetch currently playing movies
  splashMovies(): Observable<Movies> {
    return this.http.get<Movies>(`${this.apiUrl}/nowplaying`).pipe(
      catchError(this.handleError)
    );
  }

  // Handle HTTP errors
  private handleError(error: HttpErrorResponse) {
    console.error('Server returned code:', error.status);
    console.error('Response body:', error.error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }
}
