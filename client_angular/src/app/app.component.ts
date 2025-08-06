import {Component, HostListener, OnInit} from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import {AuthService} from "./services/auth.service";
import {Utilisateur} from "./models/utilisateur";
// import { AuthService } from './services/auth.service'; // si tu as un service

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  shouldShowNavbar = true;
  isLoginPage = false;
  isSignUpPage = false;
  isAdmin = false;
  isClient = false;
  user: any[]=[];
  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isLoginPage = event.url === '/login';
      this.isSignUpPage = event.url === '/signup';

      this.shouldShowNavbar = !(this.isLoginPage || this.isSignUpPage);

      this.checkUserRole();
    });
  }

  checkUserRole(): void {
    const user = this.authService.getUser();

    if (user && user.role) {
      this.user=user;
      const role = user.role.toUpperCase(); // sécurise la casse
      this.isAdmin = role === 'ADMINISTRATEUR';
      this.isClient = role === 'CLIENT';
    } else {
      this.isAdmin = false;
      this.isClient = false;
    }
  }
  isBottom = false;

  @HostListener('window:scroll', [])
  onScroll(): void {
    const threshold = 50; // marge de sécurité
    const position = window.innerHeight + window.scrollY;
    const height = document.body.offsetHeight;

    this.isBottom = position >= height - threshold;
  }

}
