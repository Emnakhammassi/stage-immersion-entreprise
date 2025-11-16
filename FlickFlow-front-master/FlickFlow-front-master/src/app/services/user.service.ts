import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, of, tap} from 'rxjs';
import {UserDto} from "./model/UserDto";
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private tokenKey = 'authToken';
  private baseUrl = 'http://localhost:8082/auth';
  private userIdKey = 'userId'; // Key for storing user ID

  constructor(private http: HttpClient, private router: Router) {}

  register(user: UserDto): Observable<UserDto> {
    return this.http.post<UserDto>(`${this.baseUrl}/signup`, user);
  }

  signIn(credentials: { email: string; password: string }): Observable<{ token: string, userId: string }> {
    return this.http.post<{ token: string, userId: string }>('http://localhost:8082/auth/signin', credentials)
      .pipe(
        tap(response => {
          localStorage.setItem(this.tokenKey, response.token);
          localStorage.setItem(this.userIdKey, response.userId); // Store user ID
        }),
        catchError(error => {
          console.error('Sign-in failed', error);
          return of({ token: '', userId: '' });
        })
      );
  }


  logout(): void {
    const token = localStorage.getItem(this.tokenKey);

    if (token) {
      this.http.post<void>('http://localhost:8082/auth/logout', {}).subscribe({
        next: () => {
          console.log('User logged out successfully');
          localStorage.removeItem(this.tokenKey);
          localStorage.removeItem(this.userIdKey);

          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Error during logout:', err);
          localStorage.removeItem(this.tokenKey); // Clear the token even if the backend fails
          localStorage.removeItem(this.userIdKey);
          this.router.navigate(['/login']);
        }
      });
    } else {
      console.log('No token found. Redirecting to login.');
      this.router.navigate(['/login']);
    }
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
  saveToken(token: string): void {
    localStorage.setItem('jwt', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  getUserId(): string | null {
    return localStorage.getItem(this.userIdKey);
  }

}
