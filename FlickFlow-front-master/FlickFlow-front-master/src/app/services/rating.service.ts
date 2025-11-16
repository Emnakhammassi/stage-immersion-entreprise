import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RatingService {
  private apiUrl = 'http://localhost:8084/movie/rating';

  constructor(private http: HttpClient) {}

  addRating(userId: string | null | undefined, movieId: number, ratingValue: number): Observable<any> {
    console.log('Adding rating with the following info:', userId, movieId, ratingValue);
    const ratingData = {
      userId: userId,
      movieId: movieId,
      ratingValue: ratingValue
    };
    return this.http.post<any>(`${this.apiUrl}`, ratingData);
  }

  getRatings(movieId:number): Observable<any> {
    console.log('Fetching all ratings for movie:', movieId);
    return this.http.get<any>(`${this.apiUrl}/movie/${movieId}`);
  }

  getUserRating(userId: string | null, movieId: number): Observable<any> {  // New method
    console.log(`Fetching rating for user ${userId} and movie ${movieId}`);
    return this.http.get<any>(`${this.apiUrl}/user/${userId}/movie/${movieId}`);
  }

}
