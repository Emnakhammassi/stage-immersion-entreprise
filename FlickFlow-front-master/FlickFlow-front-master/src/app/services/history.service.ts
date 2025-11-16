import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  private apiUrl = 'http://localhost:8087/history';

  constructor(private http: HttpClient) { }

  getUserHistory(userId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`);
  }

  saveToHistory(userId: string, movieId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/user/${userId}/movie/${movieId}`, {});
  }
}
