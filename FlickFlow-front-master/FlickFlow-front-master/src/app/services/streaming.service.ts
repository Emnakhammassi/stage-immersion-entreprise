import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StreamingService {
  private embedUrl =  'https://vidsrc.xyz/embed/movie';

  constructor(private http: HttpClient) {}

  getMovieEmbedUrl(tmdbId: number): string {
    return `${this.embedUrl}?tmdb=${tmdbId}`;
  }
}
