import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-navigation-generale',
  templateUrl: './navigation-generale.component.html',
  styleUrl: './navigation-generale.component.css'
})
export class NavigationGeneraleComponent implements OnInit {

user: any;
  isMenuOpen = false;
  showLoginModal = false;
  activeSubMenu: string | null = null;
  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.user = this.authService.getUser();
    console.log('ff', this.user);
  }
  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  openLogin() {
    this.showLoginModal = true;
  }

  toggleSubMenu(menu: string) {
    this.activeSubMenu = this.activeSubMenu === menu ? null : menu;
  }
  logout() {
    this.authService.logout();
    this.router.navigate(['/accueil']).then(() => {
      window.location.reload(); // Reloads the entire page
    });
  }

  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }
}
