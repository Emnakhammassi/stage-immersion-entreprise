import {Component, effect, inject, OnDestroy, OnInit} from '@angular/core';
import { Moviemodel } from "../../services/model/moviemodel";
import { TmdbService } from "../../services/tmdb.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-more-infos',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './more-infos.component.html',
  styleUrl: './more-infos.component.scss'
})
export class MoreInfosComponent implements OnInit, OnDestroy {

  public movieId = -1;

  tmdbService = inject(TmdbService);

  movie: Moviemodel | undefined;

  constructor() {
    effect(() => {
      this.movie = this.tmdbService.movieById().value;
    });
  }

  ngOnInit(): void {
    this.getMovieById();
  }

  getMovieById(): void {
    this.tmdbService.getMovieById(this.movieId);
  }

  ngOnDestroy(): void {
    this.tmdbService.clearGetMovieById();
  }
}
