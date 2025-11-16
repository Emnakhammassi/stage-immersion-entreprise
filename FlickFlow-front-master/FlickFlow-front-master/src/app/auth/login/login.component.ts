import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service'; // Adjust the path as necessary

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink

  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {
    this.loginForm = this.fb.group({
      email: [''],
      password: ['']
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      alert('Please fill out the form correctly.');
      return;
    }

    const { email, password } = this.loginForm.value;

    this.userService.signIn({ email, password }).subscribe(
      response => {
        console.log('User signed in successfully', response);
        localStorage.setItem('token', response.token); // Save the token
        this.router.navigate(['/home']);
      },
      error => {
        console.error('Error signing in user', error);
        if (error.status === 403) {
          alert('Invalid credentials or you do not have access.');
        } else {
          alert('An error occurred. Please try again later.');
        }
      }
    );
  }
}
