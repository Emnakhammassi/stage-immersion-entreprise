import {Component, effect, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {TmdbService} from "../../services/tmdb.service";
import {Moviemodel} from "../../services/model/moviemodel";
import {debounce, filter, interval, map} from "rxjs";
import {MovieCardComponent} from "../../home/movie-selector/movie-list/movie-card/movie-card.component";
import {NavbarComponent} from "../navbar/navbar.component";
import {MainContentComponent} from "../../home/main-content/main-content.component";
import {MovieListComponent} from "../../home/movie-selector/movie-list/movie-list.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    MovieCardComponent,
    NavbarComponent,
    MainContentComponent,
    MovieListComponent,
    NgForOf
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit {

  activatedRoute = inject(ActivatedRoute);

  tmdbService = inject(TmdbService);

  movies: Moviemodel[] | undefined;


  constructor(private router: Router) {
    effect(() => {
      let moviesResponse = this.tmdbService.search().value;
      if(moviesResponse !== undefined) {
        this.movies = moviesResponse.results;
      }
    });
  }

  ngOnInit(): void {
    this.onSearchTerm();
  }

  private onSearchTerm(): void {
    this.activatedRoute.queryParams.pipe(
      filter(queryParam => queryParam['q']),
      debounce(() => interval(300)),
      map(queryParam => queryParam['q']),
    ).subscribe({
      next: term => this.tmdbService.searchByTerm(term)
    })
  }

  goToMovieDetails(movieId: number): void {
    this.router.navigate(['/movie', movieId]);
  }
}
