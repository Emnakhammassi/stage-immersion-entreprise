import {Component, inject, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterModule, RouterOutlet} from '@angular/router';
import { routes } from './app.routes';
import {FaIconLibrary} from "@fortawesome/angular-fontawesome";
import {fontAwesomeIcons} from "./shared/font-awesome-icons";
import {NavbarComponent} from "./shared/navbar/navbar.component";
import {CommonModule} from "@angular/common";
import { SafeUrlPipe } from './shared/config/url-pipe/safe-url.pipe';
import {UserService} from "./services/user.service"; // Adjust the path as necessary

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent,  SafeUrlPipe],
  templateUrl: './app.component.html'

})



export class AppComponent  implements  OnInit{
  title = 'FlickFlow';
  faIconLibrary = inject(FaIconLibrary)
  isLoggedIn: boolean = false;
  showNavbar: boolean = false;

  constructor(private authService: UserService, private router: Router) {}


  ngOnInit(): void {
    this.initFontAwesome();
    this.isLoggedIn = this.authService.isLoggedIn();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const currentRoute = event.urlAfterRedirects;
        this.showNavbar = this.authService.isLoggedIn() && !['/login', '/signup'].includes(currentRoute);
      }
    });

  }

  private initFontAwesome() {
  this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }
}
