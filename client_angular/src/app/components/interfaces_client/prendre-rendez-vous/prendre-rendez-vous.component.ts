import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeService} from "../../../services/vehicule.service";
import {environment} from "../../../../environments/environment";
import {AuthService} from "../../../services/auth.service";
import {MagasinService} from "../../../services/magasin.service";
import {Magasin} from "../../../models/magasin";
import {RendezVous} from "../../../models/rendezVous";
import {RendezVousService} from "../../../services/rendezVous.service";

@Component({
  selector: 'app-prendre-rendez-vous',
  templateUrl: './prendre-rendez-vous.component.html',
  styleUrl: './prendre-rendez-vous.component.css'
})
export class PrendreRendezVousComponent implements OnInit {
  vehicle: any;
  today: string = new Date().toISOString().split('T')[0];
  currentUser: any;
  magasins: any;
  selectedDate!: string;
  selectedHeure!: string;
  selectedMagasinId!: number;
  message = '';
  isLoading = false;
  rdvExists: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private vehiculeService: VehiculeService,
 private router: Router, private authService: AuthService,
    private magasinService: MagasinService,
    private rdvService: RendezVousService

) {}

  ngOnInit(): void {
    this.generateHours();
    this.currentUser = this.authService.getUser();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.vehiculeService.getVehicleById(+id).subscribe({
        next: (data) => this.vehicle = data,
        error: (err) => console.error('Erreur de chargement', err)
      });
    }
    this.getMagasins();
  }
  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }
  availableHours: string[] = [];



  generateHours(): void {
    const start = 9;   // 9 AM
    const end = 17;    // 5 PM

    for (let hour = start; hour < end; hour++) {
      this.availableHours.push(`${hour.toString().padStart(2, '0')}:00`);
      this.availableHours.push(`${hour.toString().padStart(2, '0')}:30`);
    }
  }
  handleReservation(vehicleId: number) {
    const user = this.authService.getUser();
    if (user) {
      this.router.navigate(['/reservations']);
    }
  }

  getMagasins(): void {
    this.magasinService.getMagasins().subscribe(data => {
      this.magasins = data;
      console.log('jj', this.magasins);
    });
  }

  onSubmit(): void {
    if (!this.selectedDate || !this.selectedHeure || !this.selectedMagasinId) {
      this.message = 'Veuillez remplir tous les champs de rendez-vous.';
      return;
    }

    this.isLoading = true;

    this.rdvService.createWithParams(
      this.selectedDate,
      this.selectedHeure,
      this.currentUser.id, // ID du client connecté
      this.selectedMagasinId,
      this.vehicle.id
    ).subscribe({
      next: (rdv: RendezVous) => {
        // ✅ Ici on log la réponse brute du backend
        console.log('📥 Réponse brute du backend :', JSON.stringify(rdv, null, 2));

        this.message = 'Rendez-vous confirmé avec succès !';
        console.log('✅ RDV ajouté (objet TS) :', rdv);

        this.isLoading = false;

        // Appel différé de 2s
        setTimeout(() => {
          this.handleReservation(this.vehicle.id);
        }, 2000);
      },
      error: (err) => {
        console.error('❌ Erreur reçue du backend :', err);
        if (err.status === 409) {
          this.rdvExists = true;
          this.message = 'Vous avez déjà un rendez-vous pour ce véhicule en attente ou validé.';
        } else {
          this.message = 'Erreur lors de la création du rendez-vous.';
        }
        this.isLoading = false;
      }
    });
  }

  showConfirmationPanel = false;

  openConfirmationPanel() {
    this.showConfirmationPanel = true;
  }

  closeConfirmationPanel() {
    this.showConfirmationPanel = false;
  }

  confirmRendezVous() {
    this.showConfirmationPanel = false;
this.onSubmit();
  }
  closeErrorPanel(): void {
    this.rdvExists = false;
    this.message = '';
  }
}

