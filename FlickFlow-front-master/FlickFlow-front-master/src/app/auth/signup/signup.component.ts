import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserDto } from '../../services/model/UserDto';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.component.html',
  imports: [
    ReactiveFormsModule,
    NgIf,
    RouterLink,
    NgOptimizedImage,
    NgForOf
  ],
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  errorMessage: string = '';

  genres = [
    { id: 28, name: "Action" },
    { id: 12, name: "Adventure" },
    { id: 16, name: "Animation" },
    { id: 35, name: "Comedy" },
    { id: 80, name: "Crime" },
    { id: 99, name: "Documentary" },
    { id: 18, name: "Drama" },
    { id: 10751, name: "Family" },
    { id: 14, name: "Fantasy" },
    { id: 36, name: "History" },
    { id: 27, name: "Horror" },
    { id: 10402, name: "Music" },
    { id: 9648, name: "Mystery" },
    { id: 10749, name: "Romance" },
    { id: 878, name: "Science Fiction" },
    { id: 10770, name: "TV Moviemodel" },
    { id: 53, name: "Thriller" },
    { id: 10752, name: "War" },
    { id: 37, name: "Western" }
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', Validators.required],
      birthday: ['', Validators.required],
      subscriptionType: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      preferences: this.fb.array([])  // Initialize the FormArray for preferences
    });

    this.addGenreCheckboxes();
  }

  ngOnInit(): void {}

  private addGenreCheckboxes() {
    this.genres.forEach(() => {
      const control = this.fb.control(false);  // Create a checkbox control for each genre
      (this.signupForm.get('preferences') as FormArray).push(control);
    });
  }

  onSubmit() {
    const selectedGenres = this.signupForm.value.preferences
      .map((checked: boolean, i: number) => checked ? this.genres[i].name : null)
      .filter((v: string | null) => v !== null);

    const user: UserDto = {
      firstname: this.signupForm.value.firstname,
      lastname: this.signupForm.value.lastname,
      username: this.signupForm.value.username,
      birthday: this.signupForm.value.birthday,
      subscriptionType: this.signupForm.value.subscriptionType,
      email: this.signupForm.value.email,
      password: this.signupForm.value.password,
      preferences: selectedGenres  // Add the selected genres to the user object
    };

    this.userService.register(user).subscribe({
      next: (response) => {
        console.log('User registered successfully:', response);
        this.router.navigate(['/login']);  // Navigate to login after successful registration
      },
      error: (error) => {
        console.error('Registration error:', error);
        this.errorMessage = 'Failed to register user. Please try again.';
      }
    });
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.signupForm.get(controlName);
    return control ? control.hasError(errorName) && control.dirty : false;
  }

  get preferencesArray() {
    return this.signupForm.get('preferences') as FormArray;
  }
}
