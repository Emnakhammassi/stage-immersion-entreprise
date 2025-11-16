import { Component } from '@angular/core';
import {NavbarComponent} from "../shared/navbar/navbar.component";
import {MainContentComponent} from "./main-content/main-content.component";
import {RouterOutlet} from "@angular/router";
import {MovieSelectorComponent} from "./movie-selector/movie-selector.component";
import {HeroSectionComponent} from "./hero-section/hero-section.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent, MainContentComponent, RouterOutlet, MovieSelectorComponent, HeroSectionComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
