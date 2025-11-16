import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { RatingService } from '../../services/rating.service';
import { MovieDetailsComponent } from '../movie-details.component';
import { defineComponents, IgcRatingComponent } from 'igniteui-webcomponents';
import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {UserService} from "../../services/user.service";

defineComponents(IgcRatingComponent);

@Component({
  selector: 'app-rating',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  encapsulation: ViewEncapsulation.None
})

export class RatingComponent implements OnInit {
  movieId?: number;
  userId?: string | null;
  ratings: any[] = [];
  ratingValue: number = 0; // The value to be set by the user

  constructor(
    private ratingService: RatingService,
    private movieDetailsComponent: MovieDetailsComponent,
    private userService: UserService

  ) {}

  ngOnInit(): void {
    this.movieId = this.movieDetailsComponent.getMovieDetailResult?.id;

    if (this.movieId) {
      this.userId = this.userService.getUserId();
      this.getUserRating(this.userId, this.movieId);  // Fetch user's previous rating
      this.getRatings(this.movieId);
    }

  }

  ratingChanged(event: CustomEvent): void {
    const newRating = event.detail;
    this.ratingValue = newRating;
    console.log(`Rating changed to: ${newRating}`);
    this.addRating();
  }

  addRating(): void {
    if (this.movieId && this.ratingValue > 0) {
      this.ratingService.addRating(this.userId, this.movieId, this.ratingValue).subscribe({
        next: () => {
          console.log('Rating added successfully');
          this.getRatings(this.movieId!);
        },
        error: (error) => {
          console.error('Error adding rating:', error);
        }
      });
    } else {
      console.error('Movie ID or rating value is invalid');
    }
  }

  getRatings(movieId: number): void {
    this.ratingService.getRatings(movieId).subscribe({
      next: (result) => {
        this.ratings = result;
        console.log('Fetched ratings:', this.ratings);
      },
      error: (error) => {
        console.error('Error fetching ratings:', error);
      }
    });
  }

  getUserRating(userId: string | null, movieId: number): void {  // New method
    this.ratingService.getUserRating(userId, movieId).subscribe({
      next: (result) => {
        if (result && result.ratingValue) {
          this.ratingValue = result.ratingValue;
          console.log(`Fetched user rating: ${this.ratingValue}`);
        }
      },
      error: (error) => {
        console.error('Error fetching user rating:', error);
      }
    });
  }
}
