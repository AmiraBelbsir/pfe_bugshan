import { Component, OnInit } from '@angular/core';
import {RendezVousService} from "../../../services/rendezVous.service";
import {RendezVous} from "../../../models/rendezVous";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {environment} from "../../../../environments/environment";
import {AvisService} from "../../../services/avis.service";
import {Avis} from "../../../models/avis";

@Component({
  selector: 'app-liste-rendez-vous',
  templateUrl: './liste-rendez-vous.component.html',
  styleUrl: './liste-rendez-vous.component.css'
})
export class ListeRendezVousComponent implements OnInit {

  rdvs: RendezVous[] = [];
  currentUser: any;
  countEnAttente = 0;
  countValide = 0;
  countRefuse = 0;
  countTermine = 0;
  filteredRdvs: any[] = [];  // RDVs filtrés
  selectedStatus: string = "TOUS";

  constructor(private rendezvousService: RendezVousService, private avisService: AvisService,    private route: ActivatedRoute, private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
this.generateTimeSlots();

    this.selectedStatus = "TOUS";
    this.currentUser = this.authService.getUser();
    const clientId = this.currentUser.id;

    if (clientId) {
      this.rendezvousService.getRendezVousByClientId(+clientId).subscribe({
        next: (data) => {
          this.rdvs = data;
          this.filteredRdvs = data;
          console.log('RDVs du client:', data);

          // calcul des stats
          this.countEnAttente = this.rdvs.filter(r => r.statut === 'EN_ATTENTE').length;
          this.countValide    = this.rdvs.filter(r => r.statut === 'VALIDE').length;
          this.countRefuse    = this.rdvs.filter(r => r.statut === 'REFUSE').length;
          this.countTermine   = this.rdvs.filter(r => r.statut === 'TERMINE').length;
        },
        error: (err) => {
          console.error('Erreur lors du chargement des RDVs', err);
        }
      });
    }
  }


  today: string = new Date().toISOString().split('T')[0];
  hours: string[] = [];


  generateTimeSlots() {
    const start = 9; // 9 AM
    const end = 17; // 5 PM
    this.hours = [];

    for (let h = start; h <= end; h++) {
      this.hours.push(`${h.toString().padStart(2, '0')}:00`);
      if (h < end) { // avoid adding 17:30
        this.hours.push(`${h.toString().padStart(2, '0')}:30`);
      }
    }
  }

  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }


  filterByStatus(status: string) {
    this.selectedStatus = status;
    this.filteredRdvs = this.rdvs.filter(r => r.statut === status);
  }


  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'EN_ATTENTE':
        return 'bg-yellow-500/20 text-yellow-400 border border-yellow-500/40 px-3 py-1 rounded-full text-xs font-medium';
      case 'VALIDE':
        return 'bg-green-500/20 text-green-400 border border-green-500/40 px-3 py-1 rounded-full text-xs font-medium';
      case 'ANNULE':
        return 'bg-red-500/20 text-red-400 border border-red-500/40 px-3 py-1 rounded-full text-xs font-medium';
      case 'TERMINE':
        return 'bg-blue-500/20 text-blue-400 border border-blue-500/40 px-3 py-1 rounded-full text-xs font-medium';
      default:
        return 'bg-gray-500/20 text-gray-400 border border-gray-500/40 px-3 py-1 rounded-full text-xs font-medium';
    }
  }

  // Pour afficher "JUIN", "JUIL", etc.
  getMonthLabel(dateStr: string): string {
    const mois = ['JAN', 'FÉV', 'MAR', 'AVR', 'MAI', 'JUIN', 'JUIL', 'AOÛ', 'SEP', 'OCT', 'NOV', 'DÉC'];
    const date = new Date(dateStr);
    return mois[date.getMonth()];
  }

  getDay(dateStr: string): string {
    return new Date(dateStr).getDate().toString();
  }

  selectedRdv: RendezVous | null = null;  // RDV en cours d'édition
  isEditing: boolean = false;             // Pour afficher/masquer le formulaire

  startEdit(rdv: RendezVous) {
    this.selectedRdv = { ...rdv };

    // Adapter l'heure pour le select
    if (this.selectedRdv.heure) {
      this.selectedRdv.heure = this.selectedRdv.heure.substring(0,5); // garde HH:mm
    }

    this.isEditing = true;
  }

  cancelEdit() {
    this.selectedRdv = null;
    this.isEditing = false;
  }

  updateRdv() {
    if (!this.selectedRdv) return;

    this.rendezvousService.updateWithParams(
      this.selectedRdv.id,
      this.selectedRdv.date,
      this.selectedRdv.heure,
      this.selectedRdv.clientId,
      this.selectedRdv.magasinId,
      this.selectedRdv.vehiculeId,
      this.selectedRdv.statut
    ).subscribe({
      next: (updated) => {
        const index = this.rdvs.findIndex(r => r.id === updated.id);
        if (index !== -1) {
          this.rdvs[index] = updated;
          this.ngOnInit();
        }
        this.cancelEdit();
      },
      error: (err) => {
        console.error("Erreur mise à jour RDV", err);
      }
    });
    window.location.reload();
  }
  formatHeure(heure: string): string {
    return heure.substring(0,5); // "11:30"
  }


  showCancelPanel: boolean = false;
  cancelRdvId: number | null = null;

  openCancelPanel(rdvId: number) {
    this.cancelRdvId = rdvId;
    this.showCancelPanel = true;
  }

  closeCancelPanel() {
    this.showCancelPanel = false;
    this.cancelRdvId = null;
  }

  confirmCancelRdv(rdvId: number) {
// Reload the current page completely
    window.location.reload();
    this.showCancelPanel = false;
    this.rendezvousService.cancelRdv(rdvId).subscribe({
      next: (updated) => {
        // Remplacer l’ancien RDV dans la liste par celui mis à jour
        const index = this.rdvs.findIndex(r => r.id === updated.id);
        if (index !== -1) {
          this.rdvs[index] = updated;
        }
        console.log("RDV annulé avec succès !");
      },
      error: err => console.error('Erreur lors de l\'annulation', err)
    });
  }

  showReviewPanel = false;
  currentRdvId: number | null = null;

  review: Partial<Avis> = {
    note: 5,
    commentaire: ''
  };

// Ouverture du panel
  openReviewPanel(rdvId: number) {
    this.currentRdvId = rdvId;
    this.showReviewPanel = true;
  }

// Fermeture du panel
  closeReviewPanel() {
    this.showReviewPanel = false;
    this.review = { note: 5, commentaire: '' };
    this.currentRdvId = null;
  }
// Panel succès
  showSuccessPanel = false;
  successMessage = '';

  submitReview() {
    if (!this.currentRdvId) return;

    if (!this.review.commentaire || !this.review.note) {
      this.successMessage = "Veuillez renseigner la note et le commentaire.";
      this.showSuccessPanel = true;
      return;
    }

    // Récupérer le véhicule depuis le RDV courant
    const rdv = this.rdvs.find(r => r.id === this.currentRdvId);
    if (!rdv) return;

    const avisToSend: Partial<Avis> = {
      commentaire: this.review.commentaire,
      note: this.review.note,
      rdvId: this.currentRdvId,
      utilisateurId: this.currentUser.id,
      vehiculeId: rdv.vehiculeId
    };

    this.avisService.addAvis(avisToSend).subscribe({
      next: (res) => {
        this.successMessage = "Avis soumis avec succès !";
        this.showSuccessPanel = true;
       window.location.reload();
        this.closeReviewPanel(); // fermer le panel d'avis
      },
      error: (err) => {
        console.error(err);
        alert("Erreur lors de la soumission de l'avis.");
      }
    });
  }

  showViewAvisPanel = false;
  currentAvis: Avis | null = null;

// Méthode pour ouvrir le panel avec un avis existant
  viewAvis(avis: Avis) {
    this.currentAvis = avis;
    this.showViewAvisPanel = true;
  }

// Méthode pour fermer le panel
  closeViewAvisPanel() {
    this.showViewAvisPanel = false;
    this.currentAvis = null;
  }

  protected readonly encodeURIComponent = encodeURIComponent;
}
