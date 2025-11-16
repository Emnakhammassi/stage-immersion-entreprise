import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Import CommonModule
import {MovieService} from '../../services/movie.service';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-movies-splash',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    // Add CommonModule here
  ],
  templateUrl: './movies-splash.component.html',
  styleUrls: ['./movies-splash.component.css']
})
export class MoviesSplashComponent implements OnInit {
  splashResult?: any;

  constructor(private moviesService: MovieService) {}

  ngOnInit(): void {
    this.splashData();
  }

  splashData() {
    this.moviesService.splashMovies().subscribe((result) => {
      console.log(result);
      // Select a random movie from the result array
      if (result.length > 0) {
        const randomIndex = Math.floor(Math.random() * result.length);
        this.splashResult = result[randomIndex];
      }
    }, error => {
      console.error('Error fetching splash data', error);
    });
  }
}
