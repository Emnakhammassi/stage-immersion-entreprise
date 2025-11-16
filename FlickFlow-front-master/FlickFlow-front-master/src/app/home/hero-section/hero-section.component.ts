import {Component, effect, inject} from '@angular/core';
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {TmdbService} from "../../services/tmdb.service";
import {Moviemodel} from "../../services/model/moviemodel";

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [
    FaIconComponent
  ],
  templateUrl: './hero-section.component.html',
  styleUrl: './hero-section.component.css'
})
export class HeroSectionComponent {
  tmdbService = inject(TmdbService);

  trendMovie: Moviemodel | undefined;

  constructor() {
    effect(() => {
      const trendMovieResponse = this.tmdbService.fetchTrendMovie().value;
      if (trendMovieResponse) {
        this.trendMovie = trendMovieResponse.results[0];
      }
    });
  }

  ngOnInit(): void {
    this.fetchMovieTrends();
  }

  fetchMovieTrends(): void {
    this.tmdbService.getTrends();
  }
}
