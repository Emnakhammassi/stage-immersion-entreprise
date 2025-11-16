import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, of} from 'rxjs';
import { tap } from 'rxjs/operators';
import jwtDecode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8082/auth/signin'; // Auth service URL
  private tokenKey = 'authToken'; // Key for storing token
  private userUrl = 'http://localhost:8082/auth/user'; // User details URL
  private userIdKey = 'userId'; // Key for storing user ID

  constructor(private http: HttpClient) {}

  signIn(credentials: { email: string; password: string }): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(this.authUrl, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem(this.tokenKey, response.token);
          this.retrieveUserId(); // Retrieve and store user ID after sign-in
        }),
        catchError(error => {
          console.error('Sign-in failed', error);
          return of({ token: '' });
        })
      );
  }
  saveToken(token: string): void {
    localStorage.setItem('jwt', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  private retrieveUserId(): void {
    const token = localStorage.getItem(this.tokenKey);
    if (token) {
      const headers = new HttpHeaders().set('Authorization', token);
      this.http.get<{ userId: string }>(this.userUrl, { headers })
        .subscribe({
          next: response => {
            localStorage.setItem(this.userIdKey, response.userId);
          },
          error: error => {
            console.error('Failed to retrieve user ID', error);
          }
        });
    }
  }

}
