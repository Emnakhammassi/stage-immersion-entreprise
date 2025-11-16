import {Component, HostListener, inject} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {UserService} from "../../services/user.service";
import {NgClass, NgOptimizedImage} from "@angular/common";
import { faSearch, faGlobe, faReorder, faClose } from '@fortawesome/free-solid-svg-icons';
// import { IoniconsModule } from '@ionic/angular';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    FaIconComponent,
    NgOptimizedImage,
    NgClass,
    RouterLinkActive,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {


  searchTerm = '';

  router = inject(Router);

  onSearch(searchTerm: string) {
    this.searchTerm = searchTerm;
    if(this.searchTerm.length >= 1) {
      this.router.navigate(['search'], {queryParams: {q: this.searchTerm}});
    } else if (this.searchTerm.length === 0) {
      this.router.navigate(['']);
    }
  }
  constructor(private userService: UserService) {
  }

  onLogout(): void {
    this.userService.logout();
  }

  protected readonly faGlobe = faGlobe;
  protected readonly faSearch = faSearch;
  protected readonly faReorder = faReorder;
  protected readonly faClose = faClose;

  ngAfterViewInit(): void {
    const script = document.createElement('script');
    script.src = 'assets/js/navbar.js';
    script.onload = () => {
      // script loaded
    };
    document.body.appendChild(script);
  }

  isScrolled = false;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const offset = window.pageYOffset;
    if (offset > 50) { // You can adjust the scroll position value (50) as needed
      this.isScrolled = true;
    } else {
      this.isScrolled = false;
    }
  }

}
