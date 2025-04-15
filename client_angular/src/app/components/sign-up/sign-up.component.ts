import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../models/user";
import {City} from "../../models/city";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent implements OnInit {
  userAdded: boolean = false;
  successMessage: string = '';
  imageError: string | null = null;
  selectedFile: File | null = null;
  errorMessage: string = '';
  gsmInvalid = false;
  gsmTakenError = false;
  emailInvalid = false;
  emailTakenError = false;
  passwordInvalid = false;
  usernameTaken = false;
  existingUsernames: string[] = [];
  newUser: User = {
    active: false,
    address: "",
    city: "",
    email: "",
    fullName: "",
    gender: "",
    id: 0,
    imageUrl: "",
    password: "",
    phoneNumber: "",
    role: "CLIENT",
    username: ''

  };
  loading=false;
  cities = Object.keys(City);
  passwordVisible :boolean =false;
  ngOnInit(): void {
  }

  constructor(private userService: UserService, private router: Router) {
  }

  validateEmail(email: string) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.(com|ma)$/;
    if (!emailRegex.test(email)) {
      this.emailInvalid = true;
      this.errorMessage = "L'adresse email n'est pas valide.";
    } else {
      this.emailInvalid = false;
    }
  }
  validateGSM(gsm: string) {
    const gsmRegex = /^(06|07)\d{8}$/;
    if (!gsmRegex.test(gsm.trim())) {
      this.gsmInvalid = true;
      this.errorMessage = "Le numéro doit commencer par 06 ou 07 et contenir 10 chiffres.";
    }
    else {
      this.gsmInvalid = false;
    }
  }

  validatePassword(password: string) {

    const passwordRegex = /^(?=.*[0-9])(?=.*[\W_]).{6,}$/;
    if (!passwordRegex.test(password)) {
      this.passwordInvalid = true;
      this.errorMessage = "Le mot de passe doit contenir 6 caractères, dont un caractère spécial et un chiffre.";
    } else {
      this.passwordInvalid = false;
    }
  }

  togglePasswordVisibility(): void {
    this.passwordVisible = !this.passwordVisible;
    const passwordField = document.getElementById('password') as HTMLInputElement;
    if (passwordField) {
      passwordField.type = this.passwordVisible ? 'text' : 'password';
    }
  }

  checkUsername(): void {
    this.usernameTaken = this.existingUsernames.includes(this.newUser.username);
  }



  resetNewUser(): void {
    this.newUser = {
      address: "",
      city: "",
      email: "",
      fullName: "",
      gender: "",
      id: 0,
      imageUrl: "",
      password: "",
      phoneNumber: "",
      role: "",
      username: "",
      active: true

    };
    this.emailInvalid = false;
    this.gsmInvalid = false;
    this.passwordInvalid = false;
    this.emailTakenError = false;
    this.usernameTaken = false;
    this.imageError='';
    this.gsmTakenError = false;
  }


  onFileChange(event: any): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      // Vérifier le type de fichier (optionnel)
      const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
      if (!allowedTypes.includes(file.type)) {
        this.imageError = "Seuls les fichiers JPG, JPEG et PNG sont acceptés.";
        this.selectedFile = null;
        return;
      }

      // Vérifier la taille (ex: max 5MB)
      const maxSize = 5 * 1024 * 1024; // 5MB
      if (file.size > maxSize) {
        this.imageError = "La taille de l'image ne doit pas dépasser 5MB.";
        this.selectedFile = null;
        return;
      }

      this.imageError = null; // Aucune erreur
      this.selectedFile = file;
    }
  }

  onSignup(event: Event): void {
    event.preventDefault();
    this.addUser();
    this.loading = true;
  }
  addUser(): void {
    this.errorMessage = '';

    // Check if the email is invalid
    if (this.emailInvalid) {
      return;
    }


    // Check for image file errors
    if (this.imageError) {
      this.errorMessage = this.imageError; // Display image error message
      return;
    }

    // Prepare the user data to match the backend
    const userData = {
      fullName: this.newUser.fullName,
      email: this.newUser.email,
      username: this.newUser.username,
      password: this.newUser.password,
      phoneNumber: this.newUser.phoneNumber,
      gender: this.newUser.gender.toUpperCase(), // Enum handling
      role: this.newUser.role.toUpperCase(), // Enum handling
      city: this.newUser.city.toUpperCase(), // Enum handling
      address: this.newUser.address
    };

    // File handling
    const fileToSend = this.selectedFile || undefined;

    // Send request
    this.userService.createUserWithImage(userData, fileToSend).subscribe(
      (response) => {
        console.log('Utilisateur ajouté avec succès:', response);
        this.resetNewUser();
        this.userAdded = true;
        this.successMessage = 'Votre compte a été créé avec succès';
    this.loading=false;
// Masquer le message après 3 secondes, puis rediriger
        setTimeout(() => {
          this.userAdded = false;
          this.router.navigate(['/login']); // Remplace par le chemin réel
        }, 3000);
      },
      (error) => {
        this.loading=false;
        console.error("Erreur lors de l'ajout de l'utilisateur:", error);

        // Handle different errors
        if (error.status === 409 && error.error) {
          const field = error.error.field;
          const message = error.error.message;

          if (field === 'username') {
            this.usernameTaken = true;
          } else if (field === 'email') {
            this.emailTakenError = true;
          } else if (field === 'phoneNumber') {
            this.gsmTakenError = true;
          }

          this.errorMessage = message;
        } else {
          this.errorMessage = "Une erreur s'est produite lors de l'ajout de l'utilisateur.";
        }
      }
    );
  }
}
