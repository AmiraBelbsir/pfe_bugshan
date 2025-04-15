import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  shouldShowNavbar = true;
  isLoginPage = false;
  isSignUpPage = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Listen for navigation events and update the navbar visibility
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd) // Ensures only navigation changes trigger the logic
    ).subscribe((event: any) => {
      this.isLoginPage = event.url === '/login';
      this.isSignUpPage = event.url === '/signup';

      // Show navbar only if not on login or signup pages
      this.shouldShowNavbar = !(this.isLoginPage || this.isSignUpPage);
    });
  }
}
