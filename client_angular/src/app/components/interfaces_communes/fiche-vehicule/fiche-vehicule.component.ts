import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeService} from "../../../services/vehicule.service";
import {Vehicule} from "../../../models/vehicule";
import {environment} from "../../../../environments/environment";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-fiche-vehicule',
  templateUrl: './fiche-vehicule.component.html',
  styleUrl: './fiche-vehicule.component.css'
})
export class FicheVehiculeComponent implements OnInit {
  vehicle: Vehicule | null = null;
  loading: boolean = true;
  showLogin: boolean = false;
  loginOpened: boolean = false;
  signUpOpened: boolean = false;

  // Variables pour la galerie d'images
  showImageModal: boolean = false;
  modalImages: string[] = [];
  modalImageIndex: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehiculeService: VehiculeService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadVehicle();
  }



  loadVehicle(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (isNaN(id)) {
      this.loading = false;
      return;
    }

    this.vehiculeService.getVehicleById(id).subscribe({
      next: (vehicule) => {
        this.vehicle = vehicule;
        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement du véhicule:', error);
        this.loading = false;
      }
    });
  }

  goBack(): void {

  }


  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }


  handleReservation(vehicleId: number) {
    const user = this.authService.getUser();
    if (user) {
      this.router.navigate(['/reservation', vehicleId]);
    } else {
      this.showLogin=true;
    }
  }


  openLogin(): void {
    this.showLogin = false;
    this.loginOpened = true;
  }

  openRegister(): void {
    this.showLogin = false;
    this.signUpOpened = true;
  }

  // Méthodes pour obtenir les libellés des types
  getVehicleTypeLabel(type: string): string {
    const types: { [key: string]: string } = {
      'BERLINE': 'Berline',
      'SUV': 'SUV',
      'COUPE': 'Coupé',
      'CABRIOLET': 'Cabriolet',
      'CAMION': 'Camion',
      'FOURGONNETTE': 'Fourgonnette',
      'BREAK': 'Break',
      'ELECTRIQUE': 'Électrique',
      'HYBRIDE': 'Hybride',
      'PICKUP': 'Pickup',
      'MOTO': 'Moto',
      'MINIBUS': 'Minibus'
    };
    return types[type] || type;
  }

  getFuelTypeLabel(type: string): string {
    const types: { [key: string]: string } = {
      'ESSENCE': 'Essence',
      'DIESEL': 'Diesel',
      'ELECTRIQUE': 'Électrique',
      'HYBRIDE': 'Hybride'
    };
    return types[type] || type;
  }

  getTransmissionLabel(type: string): string {
    const types: { [key: string]: string } = {
      'AUTOMATIQUE': 'Automatique',
      'MANUELLE': 'Manuelle'
    };
    return types[type] || type;
  }

  // Méthodes pour la galerie d'images
  openImageModal(images: string[], index: number = 0): void {
    this.modalImages = images;
    this.modalImageIndex = index;
    this.showImageModal = true;

    // Empêcher le défilement de la page lorsque le modal est ouvert
    document.body.style.overflow = 'hidden';
  }

  closeImageModal(): void {
    this.showImageModal = false;
    this.modalImages = [];
    this.modalImageIndex = 0;

    // Rétablir le défilement de la page
    document.body.style.overflow = 'auto';
  }

  nextImage(): void {
    if (this.modalImageIndex < this.modalImages.length - 1) {
      this.modalImageIndex++;
    }
  }

  prevImage(): void {
    if (this.modalImageIndex > 0) {
      this.modalImageIndex--;
    }
  }

  setModalImageIndex(index: number): void {
    this.modalImageIndex = index;
  }

  // Gestion des événements clavier pour la navigation dans le modal
  onKeydown(event: KeyboardEvent): void {
    if (this.showImageModal) {
      if (event.key === 'Escape') {
        this.closeImageModal();
      } else if (event.key === 'ArrowRight') {
        this.nextImage();
      } else if (event.key === 'ArrowLeft') {
        this.prevImage();
      }
    }
  }
}
