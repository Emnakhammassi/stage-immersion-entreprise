import { Component, effect, inject, OnInit } from '@angular/core';
import { FaIconComponent, FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { Moviemodel } from "../../services/model/moviemodel";
import { State } from "../../services/model/state";
import { TmdbService } from "../../services/tmdb.service";
import {DatePipe, DecimalPipe, SlicePipe} from "@angular/common";

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [
    FaIconComponent, FontAwesomeModule, SlicePipe, DecimalPipe, DatePipe
  ],
  templateUrl: './main-content.component.html',
  styleUrls: ['./main-content.component.css']
})

export class MainContentComponent implements OnInit {

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
