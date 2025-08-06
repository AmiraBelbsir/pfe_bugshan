import {Component, OnInit} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {Vehicule} from "../../../models/vehicule";
import {UtilisateurService} from "../../../services/utilisateur.service";
import {VehiculeService} from "../../../services/vehicule.service";

@Component({
  selector: 'app-vehicles-list',
  templateUrl: './vehicles-list.component.html',
  styleUrl: './vehicles-list.component.css'
})
export class VehiclesListComponent implements OnInit {
  vehicles: Vehicule[] = [];
  selectedFilterType: string = '';
  selectedFilterTransmission: string = '';
  selectedFilterFuel: string = '';
  isSortedAZ: boolean = true;
  errorMessage: string = '';
  searchTerm = '';
  vehicleAdded: boolean = false;
  vehicleUpdated: boolean = false;
  successMessage: string = '';
  filteredVehicles = [...this.vehicles];
  imageError: string | null = null;
  currentImage: string | undefined;

  nouveauVehicule: Vehicule = {
    id: 0,
    urlImage: "",
    marque: "",
    modele: "",
    quantite: 0,
    annee: 0,
    couleur: "",
    sieges: 0,
    prix: 0,
    disponible: false,
    emplacement: "",
    niveauCarburant: 0,
    typeVehicule: "",
    typeCarburant: "",
    typeTransmission: "",
    photosAdditionnelles: []
  };



  showAddPanel = false; // Contrôle l'affichage du panneau d'ajout
  selectedVehicle: Vehicule | null = null; // Stocke les détails du véhicule sélectionné
  showEditPanel: boolean = false;
  isEditing: boolean = false;
  selectedFile: File | null = null;
  years: number [] = [];

  constructor(private vehicleService: VehiculeService) {

  }


  ngOnInit() {
  this.fetchVehicles();
  this.fetchYears();
  }

  fetchYears(): void{
    const currentYear = new Date().getFullYear();
    for (let year = currentYear; year >= 2000; year--) {
      this.years.push(year);
    }
}

  fetchVehicles(): void {
    this.vehicleService.getAllVehicles().subscribe({
      next: (data) => {
        this.vehicles = data;
        this.filteredVehicles = data;
      },
      error: (err) => {
        console.error('Error fetching vehicles:', err);
        this.errorMessage = 'Échec du chargement des véhicules';
      }
    });
  }
  viewDetails(vehicleId: number): void {
    this.vehicleService.getVehicleById(vehicleId).subscribe({
      next: (vehicle) => {
        this.selectedVehicle = { ...vehicle }; // Clone the object to prevent unwanted changes
        this.currentImage = vehicle.urlImage;  // Affiche l’image principale par défaut
        this.showEditPanel = true;
        this.isEditing = false; // Ensure it's in view mode by default
      },
      error: (err) => {
        console.error('Error fetching vehicle details:', err);
      }
    });
  }


  resetNewVehicle(): void {
    this.nouveauVehicule= {
      id: 0,
      urlImage: "",
      marque: "",
      modele: "",
      quantite: 0,
      annee: 0,
      couleur: "",
      sieges: 0,
      prix: 0,
      disponible: false,
      emplacement: "",
      niveauCarburant: 0,
      typeVehicule: "",
      typeCarburant: "",
      typeTransmission: "",
      photosAdditionnelles: []
    };


    this.imageError = '';
  }

  validateLicensePlate(plate: string) {
    const plateRegex = /^\d{4}-[\u0600-\u06FF]-\d{2}$/;
    if (!plateRegex.test(plate.trim())) {
      this.errorMessage = "Le numéro d'immatriculation doit être sous la forme 1234-أ-56.";
      return false;
    }
    return true;
  }



  validateFuelLevel(level: number) {
    if (level < 0 || level > 100) {
      this.errorMessage = "Le niveau de carburant doit être entre 0 et 100%.";
      return false;
    }
    return true;
  }

  filterVehicles(): void {
    this.filteredVehicles = this.vehicles.filter(vehicle => {
      const matchesType = !this.selectedFilterType || vehicle.typeVehicule === this.selectedFilterType;
      const matchesTransmission = !this.selectedFilterTransmission || vehicle.typeTransmission === this.selectedFilterTransmission;
      const matchesFuel = !this.selectedFilterFuel || vehicle.typeCarburant === this.selectedFilterFuel;
      const matchesSearch = !this.searchTerm || vehicle.marque.toLowerCase().includes(this.searchTerm.toLowerCase());

      return matchesType && matchesTransmission && matchesFuel && matchesSearch;
    });
  }

  closePanel(): void {
    this.showEditPanel = false;
    this.resetNewVehicle();
  }

  enableEditing(): void {
    this.isEditing = true;
  }





  sortByModel(): void {
    this.isSortedAZ = !this.isSortedAZ;
    this.filteredVehicles.sort((a, b) => this.isSortedAZ
      ? a.modele.localeCompare(b.modele)
      : b.modele.localeCompare(a.modele)
    );
  }


  togglePanel(): void {
    this.showAddPanel = !this.showAddPanel;
    this.resetNewVehicle();
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


  addVehicle(): void {
    this.errorMessage = '';

    // Vérifier les erreurs d'image
    if (this.imageError) {
      this.errorMessage = this.imageError; // Afficher le message d'erreur d'image
      return;
    }

    // Préparer les données du véhicule à envoyer
    const donneesVehicule = {
      marque: this.nouveauVehicule.marque,
      modele: this.nouveauVehicule.modele,
      quantite: this.nouveauVehicule.quantite,
      annee: this.nouveauVehicule.annee,
      couleur: this.nouveauVehicule.couleur,
      sieges: this.nouveauVehicule.sieges,
      prix: this.nouveauVehicule.prix,
      disponible: this.nouveauVehicule.disponible,
      emplacement: this.nouveauVehicule.emplacement,
      niveauCarburant: this.nouveauVehicule.niveauCarburant,
      typeVehicule: this.nouveauVehicule.typeVehicule.toUpperCase(),
      typeCarburant: this.nouveauVehicule.typeCarburant.toUpperCase(),
      typeTransmission: this.nouveauVehicule.typeTransmission.toUpperCase(),
      photosAdditionnelles: this.nouveauVehicule.photosAdditionnelles
    };


    // Gestion du fichier image
    const fileToSend = this.selectedFile || undefined;

    // Envoi de la requête au service
    this.vehicleService.createVehicule(donneesVehicule, fileToSend).subscribe(
      (response) => {
        console.log('Véhicule ajouté avec succès:', response);
        this.vehicles.push(response);
        this.resetNewVehicle();
        this.vehicleAdded = true;
        this.showAddPanel = false;
        this.successMessage = 'Véhicule ajouté avec succès';

        // Masquer le message de succès après 3 secondes
        setTimeout(() => {
          this.vehicleAdded = false;
        }, 3000);
      },
      (error) => {
        if (error.status === 409 && error.error) {
          const field = error.error.field;
          const message = error.error.message;

          this.errorMessage = message;
        }
        else {
          console.error("Erreur lors de l'ajout du véhicule:", error);
          this.errorMessage = "Une erreur s'est produite lors de l'ajout du véhicule.";
        }});
  }

  updateVehicle(): void {
    // Vérifier si un véhicule est sélectionné
    if (!this.selectedVehicle || this.selectedVehicle.id === undefined) {
      this.errorMessage = 'Aucun véhicule sélectionné pour la mise à jour!';
      return;
    }

    // Vérifier les champs obligatoires
    if (!this.selectedVehicle.marque || !this.selectedVehicle.modele || !this.selectedVehicle.annee) {
      this.errorMessage = 'Les champs marque, modèle, année et immatriculation sont obligatoires';
      return;
    }

    // Gestion du fichier image
    const fileToSend = this.selectedFile === null ? undefined : this.selectedFile;

    // Vérifier les erreurs d'image
    if (this.imageError) {
      this.errorMessage = this.imageError; // Afficher le message d'erreur d'image
      return;
    }

    // Préparer les données du véhicule pour la mise à jour
    const donneesVehicule = {
      marque: this.selectedVehicle.marque,
      modele: this.selectedVehicle.modele,
      quantite: this.nouveauVehicule.quantite,
      annee: this.selectedVehicle.annee,
      couleur: this.selectedVehicle.couleur,
      sieges: this.selectedVehicle.sieges,
      prix: this.selectedVehicle.prix,
      disponible: this.selectedVehicle.disponible,
      emplacement: this.selectedVehicle.emplacement,
      niveauCarburant: this.selectedVehicle.niveauCarburant,
      typeVehicule: this.selectedVehicle.typeVehicule,
      typeCarburant: this.selectedVehicle.typeCarburant,
      typeTransmission: this.selectedVehicle.typeTransmission,
      photosSupplementaires: this.selectedVehicle.photosAdditionnelles
    };

    // Envoi de la requête de mise à jour
    this.vehicleService.updateVehicule(this.selectedVehicle.id, donneesVehicule, fileToSend).subscribe(
      (updatedVehicle) => {
        // Mettre à jour la liste locale des véhicules
        const index = this.vehicles.findIndex(vehicle => vehicle.id === updatedVehicle.id);
        if (index !== -1) {
          this.vehicles[index] = updatedVehicle;
        }

        this.resetNewVehicle();
        this.fetchVehicles();
        this.vehicleUpdated = true;
        this.showEditPanel = false;
        this.successMessage = 'Véhicule modifié avec succès';

        // Masquer le message de succès après 3 secondes
        setTimeout(() => {
          this.vehicleUpdated = false;
        }, 3000);
      },
      (error) => {
        if (error.status === 409 && error.error) {
          const field = error.error.field;
          const message = error.error.message;

          this.errorMessage = message;
        }
        else {
        console.error("Erreur lors de la mise à jour du véhicule:", error);
        this.errorMessage = 'Échec de la mise à jour du véhicule.';
      }}
    );
  }

  setCurrentImage(imageUrl: string): void {
    this.currentImage = imageUrl;
  }

  vehicleTypeToLabel(type: string): string {
    const map: { [key: string]: string } = {
      SEDAN: 'Berline',
      SUV: 'Véhicule utilitaire sport',
      COUPE: 'Coupé',
      CONVERTIBLE: 'Cabriolet',
      TRUCK: 'Camion',
      VAN: 'Monospace / Fourgonnette',
      WAGON: 'Break',
      ELECTRIC: 'Véhicule électrique',
      HYBRID: 'Véhicule hybride',
      PICKUP: 'Pickup',
      MOTORCYCLE: 'Moto',
      MINIVAN: 'Minivan'
    };
    return map[type] || type;
  }

  fuelTypeToLabel(fuelType?: string): string {
    const map: { [key: string]: string } = {
      GAS: 'Essence',
      DIESEL: 'Diesel',
      ELECTRIC: 'Électrique',
      HYBRID: 'Hybride'
    };
    return fuelType ? map[fuelType] || fuelType : 'Inconnu';
  }

  transmissionTypeToLabel(type?: string): string {
    const map: { [key: string]: string } = {
      MANUAL: 'Manuelle',
      AUTOMATIC: 'Automatique'
    };
    return type ? map[type] || type : 'Inconnu';
  }




  onAdditionalPhotosChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input?.files) {
      const files = Array.from(input.files);
      const fileReaders: Promise<any>[] = files.map((file) => {
        return new Promise((resolve, reject) => {
          const reader = new FileReader();
          reader.onload = (e) => resolve(reader.result);
          reader.onerror = (err) => reject(err);
          reader.readAsDataURL(file);
        });
      });

      // Wait for all files to be read and update the additional photos array
      Promise.all(fileReaders)
        .then((photoUrls) => {
          // Ensure additionalPhotos is defined and append new URLs
          this.nouveauVehicule.photosAdditionnelles = [
            ...(this.nouveauVehicule.photosAdditionnelles || []),
            ...photoUrls
          ];
        })
        .catch((error) => {
          console.error('Error reading files:', error);
        });
    }
  }

  // Remove a specific additional photo
  removeAdditionalPhoto(photoUrl: string) {
    // Ensure additionalPhotos is defined before attempting to filter
    this.nouveauVehicule.photosAdditionnelles = (this.nouveauVehicule.photosAdditionnelles || []).filter(
      (photo) => photo !== photoUrl
    );
  }
}
