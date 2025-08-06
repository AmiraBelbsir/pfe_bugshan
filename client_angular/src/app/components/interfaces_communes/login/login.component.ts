import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from "../../../services/auth.service";

import { FormsModule } from "@angular/forms";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  loading: boolean = false; // Added loading state
  showLoginModal: boolean = true;
  showSignUpModal: boolean = false;
  constructor(private loginService: AuthService, private router: Router) {}

  onLogin(event: SubmitEvent) {
    event.preventDefault();
    this.errorMessage = '';  // Clear previous errors
    this.loading = true;  // Start loading

    this.loginService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        this.loading = false;  // Stop loading
        if (!response.actif) {
          this.errorMessage = "Votre compte est désactivé. Veuillez contacter l'administrateur.";
          return;
        }

        localStorage.setItem('user', JSON.stringify(response));

        switch (response.role) {
          case "ADMINISTRATEUR":
            this.router.navigate(['/utilisateurs/liste']);
            break;
          case "CLIENT":
            this.router.navigate(['/accueil']).then(() => {
              window.location.reload(); // Reloads the entire page
            });
            break;
          case "RESPONSABLE":
            this.router.navigate(['/equipements/list']);
            break;
          default:
            this.errorMessage = "Rôle non reconnu.";
        }
        this.showLoginModal=false;
      },

      error: (error) => {
        this.loading = false;  // Stop loading on error
        console.error('Login error:', error);
        this.errorMessage = 'Identifiants incorrects, veuillez réessayer.';
      }
    });
  }

  openSignUp(){

    this.showSignUpModal=true;
    console.log("hh", this.showSignUpModal);
  }

  closeLogin(){
   this.showLoginModal = false;
    this.showSignUpModal=false;
  }

}
