import {Component, OnInit} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {Vehicle} from "../../../models/vehicle";
import {UserService} from "../../../services/user.service";
import {VehicleService} from "../../../services/vehicle.service";

@Component({
  selector: 'app-vehicles-list',
  templateUrl: './vehicles-list.component.html',
  styleUrl: './vehicles-list.component.css'
})
export class VehiclesListComponent implements OnInit {
  vehicles: Vehicle[] = [];
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
  vinTaken: boolean = false;
  currentImage: string | undefined;
  newVehicle: Vehicle = {
    id: 0,
    imageUrl: "",
    make: "",
    model: "",
    quantity: 0,
    year: 0,
    color: "",
    vin: "",
    mileage: 0,
    seats: 0,
    retailPrice: 0,
    available: false,
    location: "",
    insured: false,
    vehicleCondition: "",
    fuelLevel: 0,
    vehicleType: "",
    fuelType: "",
    transmissionType: "",
    additionalPhotos: []
  };

  showAddPanel = false; // Contrôle l'affichage du panneau d'ajout
  selectedVehicle: Vehicle | null = null; // Stocke les détails du véhicule sélectionné
  showEditPanel: boolean = false;
  isEditing: boolean = false;
  selectedFile: File | null = null;
  years: number [] = [];

  constructor(private vehicleService: VehicleService) {

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
        this.currentImage = vehicle.imageUrl;  // Affiche l’image principale par défaut
        this.showEditPanel = true;
        this.isEditing = false; // Ensure it's in view mode by default
      },
      error: (err) => {
        console.error('Error fetching vehicle details:', err);
      }
    });
  }


  resetNewVehicle(): void {
    this.newVehicle = {
      id: 0,
      imageUrl: "",
      make: "",
      model: "",
      quantity: 0,
      year: 0,
      color: "",
      vin: "",
      mileage: 0,
      seats: 0,
      retailPrice: 0,
      available: false,
      location: "",
      insured: false,
      vehicleCondition: "",
      fuelLevel: 0,
      vehicleType: "",
      fuelType: "",
      transmissionType: "",
      additionalPhotos: []
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


  validateVIN(vin: string) {
    const vinRegex = /^[A-HJ-NPR-Z0-9]{17}$/;
    if (!vinRegex.test(vin.trim())) {
      this.errorMessage = "Le VIN doit contenir exactement 17 caractères alphanumériques sans les lettres I, O et Q.";
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
      const matchesType = !this.selectedFilterType || vehicle.vehicleType === this.selectedFilterType;
      const matchesTransmission = !this.selectedFilterTransmission || vehicle.transmissionType === this.selectedFilterTransmission;
      const matchesFuel = !this.selectedFilterFuel || vehicle.fuelType === this.selectedFilterFuel;
      const matchesSearch = !this.searchTerm || vehicle.make.toLowerCase().includes(this.searchTerm.toLowerCase());

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
      ? a.model.localeCompare(b.model)
      : b.model.localeCompare(a.model)
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
    const vehicleData = {
      make: this.newVehicle.make,
      model: this.newVehicle.model,
      quantity: this.newVehicle.quantity,
      year: this.newVehicle.year,
      color: this.newVehicle.color,
      vin: this.newVehicle.vin,
      mileage: this.newVehicle.mileage,
      seats: this.newVehicle.seats,
      retailPrice: this.newVehicle.retailPrice,
      available: this.newVehicle.available,
      location: this.newVehicle.location,
      insured: this.newVehicle.insured,
      vehicleCondition: this.newVehicle.vehicleCondition.toUpperCase(),
      fuelLevel: this.newVehicle.fuelLevel,
      vehicleType: this.newVehicle.vehicleType.toUpperCase(),
      fuelType: this.newVehicle.fuelType.toUpperCase(),
      transmissionType: this.newVehicle.transmissionType.toUpperCase(),
      additionalPhotos: this.newVehicle.additionalPhotos // Ajouter les photos supplémentaires
    };

    // Gestion du fichier image
    const fileToSend = this.selectedFile || undefined;

    // Envoi de la requête au service
    this.vehicleService.createVehicle(vehicleData, fileToSend).subscribe(
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

          if (field === 'vin') {
            this.vinTaken = true;
          }
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
    if (!this.selectedVehicle.make || !this.selectedVehicle.model || !this.selectedVehicle.year) {
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
    const vehicleData = {
      make: this.selectedVehicle.make,
      model: this.selectedVehicle.model,
      quantity: this.newVehicle.quantity,
      year: this.selectedVehicle.year,
      color: this.selectedVehicle.color,
      vin: this.selectedVehicle.vin,
      mileage: this.selectedVehicle.mileage,
      seats: this.selectedVehicle.seats,
      retailPrice: this.selectedVehicle.retailPrice,
      available: this.selectedVehicle.available,
      location: this.selectedVehicle.location,
      insured: this.selectedVehicle.insured,
      vehicleCondition: this.selectedVehicle.vehicleCondition,
      fuelLevel: this.selectedVehicle.fuelLevel,
      vehicleType: this.selectedVehicle.vehicleType,
      fuelType: this.selectedVehicle.fuelType,
      transmissionType: this.selectedVehicle.transmissionType,
      additionalPhotos: this.selectedVehicle.additionalPhotos // Ajouter les photos supplémentaires
    };

    // Envoi de la requête de mise à jour
    this.vehicleService.updateVehicle(this.selectedVehicle.id, vehicleData, fileToSend).subscribe(
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

          if (field === 'vin') {
            this.vinTaken = true;
          }
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


  conditionToLabel(condition?: string): string {
    const map: { [key: string]: string } = {
      EXCELLENT: 'Excellent',
      GOOD: 'Bon',
      FAIR: 'Moyen',
      NEEDS_MAINTENANCE: 'Nécessite un entretien'
    };
    return condition ? map[condition] || condition : 'Inconnu';
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
          this.newVehicle.additionalPhotos = [
            ...(this.newVehicle.additionalPhotos || []),
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
    this.newVehicle.additionalPhotos = (this.newVehicle.additionalPhotos || []).filter(
      (photo) => photo !== photoUrl
    );
  }
}
