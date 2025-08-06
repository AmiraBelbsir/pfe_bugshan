import { Component, OnInit } from '@angular/core';
import {Utilisateur} from "../../../models/utilisateur";
import {UtilisateurService} from "../../../services/utilisateur.service";
import {environment} from "../../../../environments/environment";
import {Ville} from "../../../models/ville";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css'
})
export class UsersListComponent implements OnInit{

  users: Utilisateur[] = [];
  selectedFilter: string = '';
  isSortedAZ: boolean = true;
  errorMessage: string = '';
  isSearchOpen = false;
  searchTerm = '';
  userAdded: boolean = false;
  userUpdated: boolean = false;
  successMessage: string = '';
  filteredUsers = [...this.users];
  imageError: string | null = null;
  cities = Object.keys(Ville);
  newUser: Utilisateur = {
    id: 0,
    nomComplet: "",
    numeroTelephone: "",
    motDePasse: "",
    email: "",
    role: "",
    sexe: "",
    urlImage: "",
    actif: false,
    nomUtilisateur: "",
    adresse: "",
    ville: ""
  };

  passwordVisible = false;
  showAddPanel = false; // Controls the panel visibility
  usernameTaken = false;
  gsmInvalid = false;
  gsmTakenError = false;
  emailInvalid = false;
  emailTakenError = false;
  passwordInvalid = false;
  existingUsernames: string[] = [];
  selectedUser: Utilisateur | null = null; // Store selected user details
  showEditPanel: boolean = false;
  isEditing: boolean = false;
  selectedFile: File | null = null;
  selectedActiveStatus: string='';


  constructor(private userService: UtilisateurService) {
  }


  viewDetails(userId: number): void {
    this.userService.getUserById(userId).subscribe({
      next: (user) => {
        this.selectedUser = { ...user }; // Clone the object to prevent unwanted changes
        this.showEditPanel = true;
        this.isEditing = false; // Ensure it's in view mode by default
      },
      error: (err) => {
        console.error('Error fetching user details:', err);
      }
    });
  }

// Method to toggle edit mode
  enableEditing(): void {
    this.isEditing = true;
  }






// Method to close the panel
  closePanel(): void {
    this.showEditPanel = false;
    this.resetNewUser();
  }

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.filteredUsers = data;
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        this.errorMessage = 'Failed to load users';
      }
    });
  }

  checkUsername(): void {
    this.usernameTaken = this.existingUsernames.includes(this.newUser.nomUtilisateur);
  }




  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
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

// Prépare les données utilisateur selon le backend (attributs en français)
    const userData = {
      nomComplet: this.newUser.nomComplet,
      email: this.newUser.email,
      nomUtilisateur: this.newUser.nomUtilisateur,
      motDePasse: this.newUser.motDePasse,
      numeroTelephone: this.newUser.numeroTelephone,
      sexe: this.newUser.sexe.toUpperCase(),   // Gestion enum
      role: this.newUser.role.toUpperCase(),   // Gestion enum
      ville: this.newUser.ville.toUpperCase(), // Gestion enum
      adresse: this.newUser.adresse
    };

    // File handling
    const fileToSend = this.selectedFile || undefined;

    // Send request
    this.userService.createUtilisateurAvecImage(userData, fileToSend).subscribe(
      (response) => {
        console.log('Utilisateur ajouté avec succès:', response);
        this.users.push(response);
        this.resetNewUser();
        this.userAdded = true;
        this.showAddPanel = false;
        this.successMessage = 'Utilisateur ajouté avec succès';

        // Hide success message after 3 seconds
        setTimeout(() => {
          this.userAdded = false;
        }, 3000);
      },
      (error) => {
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


  updateUser(): void {
    // Check if the selected user exists and is properly selected
    if (!this.selectedUser || this.selectedUser.id === undefined) {
      this.errorMessage = 'Aucun utilisateur sélectionné pour la mise à jour!';
      return;
    }

    // Check for missing required fields
    if (!this.selectedUser.nomComplet || !this.selectedUser.email || !this.selectedUser.numeroTelephone || !this.selectedUser.nomUtilisateur) {
      this.errorMessage = 'Les champs nom, email, numéro de téléphone et nom d\'utilisateur sont obligatoires';
      return;
    }

    // Check if a file has been selected (if required for the update)
    const fileToSend = this.selectedFile === null ? undefined : this.selectedFile;

    // Check for image file errors
    if (this.imageError) {
      this.errorMessage = this.imageError; // Display image error message
      return;
    }

// Préparer les données utilisateur pour la mise à jour (uniquement champs modifiés)
    const userData = {
      nomComplet: this.selectedUser.nomComplet,
      email: this.selectedUser.email,
      nomUtilisateur: this.selectedUser.nomUtilisateur,
      numeroTelephone: this.selectedUser.numeroTelephone,
      sexe: this.selectedUser.sexe,
      role: this.selectedUser.role,
      ville: this.selectedUser.ville,
      adresse: this.selectedUser.adresse,
      actif: this.selectedUser.actif
    };


    // Call the service to update the user
    this.userService.updateUtilisateur(
      this.selectedUser.id,
      userData,
      fileToSend
    ).subscribe(
      (updatedUser) => {
        // Find and update the user in the users list
        const index = this.users.findIndex(user => user.id === updatedUser.id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }

        this.resetNewUser();
        this.fetchUsers();
        this.userUpdated = true;
        this.showEditPanel = false;
        this.successMessage = 'Utilisateur modifié avec succès';

        // Hide the success message after 3 seconds
        setTimeout(() => {
          this.userUpdated = false;
        }, 3000);
      },
      (error) => {
        // Handle specific error cases
        if (error.status === 409 && error.error) {
          // Check for conflict errors (username, email, etc.)
          const field = error.error.field;
          const message = error.error.message;

          if (field === 'username') {
            this.usernameTaken = true;
            this.errorMessage = message;
          } else if (field === 'email') {
            this.emailTakenError = true;
            this.errorMessage = message;
          } else if (field === 'phoneNumber') {
            this.gsmTakenError = true;
            this.errorMessage = message;
          } else {
            this.errorMessage = message || 'Un conflit est survenu lors de la mise à jour.';
          }
        } else if (error.status === 400) {
          // Handle bad request error (possibly due to invalid data)
          this.errorMessage = 'Des données invalides ont été envoyées. Veuillez vérifier et réessayer.';
        } else if (error.status === 404) {
          // Handle not found error (user not found)
          this.errorMessage = 'Utilisateur non trouvé.';
        } else {
          // Generic error fallback
          this.errorMessage = 'Échec de la mise à jour de l\'utilisateur.';
        }
      }
    );
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


  resetNewUser(): void {
    this.newUser = {
      id: 0,
      nomComplet: "",
      numeroTelephone: "",
      motDePasse: "",
      email: "",
      role: "",
      sexe: "",
      urlImage: "",
      actif: true,
      nomUtilisateur: "",
      adresse: "",
      ville: ""
    };

    this.emailInvalid = false;
    this.gsmInvalid = false;
    this.passwordInvalid = false;
    this.emailTakenError = false;
    this.usernameTaken = false;
    this.imageError='';
    this.gsmTakenError = false;
  }

  togglePasswordVisibility(): void {
    this.passwordVisible = !this.passwordVisible;
    const passwordField = document.getElementById('password') as HTMLInputElement;
    if (passwordField) {
      passwordField.type = this.passwordVisible ? 'text' : 'password';
      passwordField.type = this.passwordVisible ? 'text' : 'password';
    }
  }

  togglePanel(): void {
    this.showAddPanel = !this.showAddPanel;
    this.resetNewUser();
  }



  sortByName(): void {
    this.isSortedAZ = !this.isSortedAZ;
    this.filterUsers();
  }

  filterUsers(): void {
    this.filteredUsers = this.users
      .filter(user => {
        const matchesName = !this.searchTerm || user.nomComplet.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesRole = !this.selectedFilter || user.role === this.selectedFilter;
        const matchesActive = !this.selectedActiveStatus ||
          (this.selectedActiveStatus === 'active' && user.actif) ||
          (this.selectedActiveStatus === 'inactive' && !user.actif);
        return matchesName && matchesRole && matchesActive;
      })
      .sort((a, b) => this.isSortedAZ
        ? a.nomComplet.localeCompare(b.nomComplet)
        : b.nomComplet.localeCompare(a.nomComplet)
      );
  }




  /*exportToExcel()
    :
    void {
    // Liste des utilisateurs avec leurs attributs
    const worksheetData = this.users.map(user => ({
      'ID': user.id,
      'Nom': user.nom,
      'Username': user.username,
      'Email': user.email,
      'Téléphone': user.gsm,
      'Civilité': user.civilite,
      'Rôle': user.role,
      'Actif': user.actif ? 'Oui' : 'Non',
      'Date Inscription': new Date(user.dateInscription).toLocaleDateString(),
    }));

    const worksheet = XLSX.utils.json_to_sheet(worksheetData);

    // 🎨 Styles pour l'entête (Vert foncé, texte blanc)
    const headerStyle = {
      font: {bold: true, color: {rgb: 'FFFFFF'}},
      fill: {fgColor: {rgb: '2E7D32'}}, // Vert foncé
      alignment: {horizontal: 'center', vertical: 'center'},
      border: {top: {style: 'thin'}, bottom: {style: 'thin'}}
    };

    // 🎨 Couleurs alternées pour les lignes (vert clair)
    const evenRowStyle = {fill: {fgColor: {rgb: 'E8F5E9'}}}; // Vert clair
    const oddRowStyle = {fill: {fgColor: {rgb: 'C8E6C9'}}}; // Vert plus foncé

    // 🎨 Bordures des cellules
    const borderStyle = {
      border: {
        top: {style: 'thin', color: {rgb: '000000'}},
        bottom: {style: 'thin', color: {rgb: '000000'}},
        left: {style: 'thin', color: {rgb: '000000'}},
        right: {style: 'thin', color: {rgb: '000000'}}
      }
    };

    // 🔍 Appliquer les styles
    const range = XLSX.utils.decode_range(worksheet['!ref'] || '');

    for(let col = range.s.c; col <= range.e.c;
        col++
    )
    {
      const headerCell = XLSX.utils.encode_cell({r: 0, c: col});
      if (worksheet[headerCell]) {
        worksheet[headerCell].s = headerStyle;
      }
    }

    for (let row = 1; row <= range.e.r; row++) {
      for (let col = range.s.c; col <= range.e.c; col++) {
        const cell = XLSX.utils.encode_cell({r: row, c: col});
        if (worksheet[cell]) {
          worksheet[cell].s = {
            ...borderStyle,
            ...(row % 2 === 0 ? evenRowStyle : oddRowStyle)
          };
        }
      }
    }

// 🔧 Ajuster la largeur des colonnes
    worksheet['!cols'] = [
      {wch: 5},  // ID
      {wch: 20}, // Nom
      {wch: 20}, // Username
      {wch: 30}, // Email
      {wch: 15}, // Téléphone
      {wch: 10}, // Civilité
      {wch: 15}, // Rôle
      {wch: 10}, // Actif
      {wch: 20}  // Date Inscription
    ];

    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Utilisateurs');

    const excelBuffer = XLSX.write(workbook, {bookType: 'xlsx', type: 'array'});
    const data = new Blob([excelBuffer], {type: 'application/octet-stream'});
    saveAs(data, 'utilisateurs.xlsx');
  }
*/

}
