import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import {HomeComponent} from "./home/home.component";
import {SearchComponent} from "./shared/search/search.component";
import {authGuard} from "./shared/config/guards/auth.guard";
import {MovieDetailsComponent} from "./movie-details/movie-details.component";
import {MyListComponent} from "./my-list/my-list.component";
import {NgModule} from "@angular/core";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: 'search', component: SearchComponent, canActivate: [authGuard] },
  { path: 'search/home', component: HomeComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  { path: 'movies/:id', component: MovieDetailsComponent , canActivate: [authGuard]},
  { path: 'movie/:id', component: MovieDetailsComponent, canActivate: [authGuard] },// Route for movie details with id parameter
  { path: 'mylist', component: MyListComponent , canActivate: [authGuard]},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
