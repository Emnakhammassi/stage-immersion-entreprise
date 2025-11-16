import {Component, inject, Input} from '@angular/core';
import { Moviemodel } from "../../../../services/model/moviemodel";
import { TmdbService } from "../../../../services/tmdb.service";
import {Router} from "@angular/router";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})
export class MovieCardComponent {

  @Input() movie: Moviemodel | undefined;

  constructor(private router: Router, protected tmdbService: TmdbService) { }

  goToMovieDetails(): void {
    if (this.movie?.id) {
      this.router.navigate(['/movie', this.movie.id]);
    }
  }

  getImageURL(poster_path: string) {
    return this.tmdbService.getImageURL(poster_path, 'w500');

  }
}
