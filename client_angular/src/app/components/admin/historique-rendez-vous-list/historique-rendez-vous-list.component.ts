import { Component } from '@angular/core';
import {RendezVousService} from "../../../services/rendezVous.service";

import {RendezVous} from "../../../models/rendezVous";

@Component({
  selector: 'app-historique-rendez-vous-list',
  templateUrl: './historique-rendez-vous-list.component.html',
  styleUrl: './historique-rendez-vous-list.component.css'
})
export class HistoriqueRendezVousListComponent {
  rendezVousList: RendezVous[] = [];
  searchTerm: string = '';  // Pour la recherche

  constructor(private rdvService: RendezVousService) {}


  searchClient: string = '';
  searchVehicule: string = '';
  searchCommercial: string = '';
  searchDate: string = '';
  selectedStatut: string = '';
  startDate: string = '';
  endDate: string = '';





  ngOnInit(): void {
    this.loadRendezVous();
  }

  loadRendezVous() {
    this.rdvService.getAll().subscribe({
      next: (data) => {
        // ⬇️ Filtrage par statut
        this.rendezVousList = data;

      },
      error: (err) => console.error('Erreur chargement RDV', err)
    });
  }


  onFilterChange() {

  }
  get filteredRendezVousList(): RendezVous[] {
    return this.rendezVousList.filter(rdv => {
      const rdvDate = rdv.date ? new Date(rdv.date) : null;
      const start = this.startDate ? new Date(this.startDate) : null;
      const end = this.endDate ? new Date(this.endDate) : null;





      // Conditions recherche texte et statut (à adapter selon ce que tu as)
      const matchesClient = !this.searchClient || rdv.clientFullName?.toLowerCase().includes(this.searchClient.toLowerCase());
      const matchesVehicule = !this.searchVehicule || rdv.vehiculeMakeModel?.toLowerCase().includes(this.searchVehicule.toLowerCase());
      const matchesCommercial = !this.searchCommercial || rdv.commercialFullName?.toLowerCase().includes(this.searchCommercial.toLowerCase());
      const matchesStatut =
        this.selectedStatut
          ? rdv.statut === this.selectedStatut
          : rdv.statut && rdv.statut !== 'EN_ATTENTE';



      // Filtrage date par plage
      let matchesDate = true;
      if (rdvDate && start) {
        matchesDate = rdvDate >= start;
      }
      if (rdvDate && end) {
        matchesDate = matchesDate && rdvDate <= end;
      }

      return matchesClient && matchesVehicule && matchesCommercial && matchesDate && matchesStatut;
    });
  }



  validerRdv(rdv: RendezVous) {
    rdv.statut = 'VALIDE';
    this.updateRdv(rdv);
  }

  refuserRdv(rdv: RendezVous) {
    rdv.statut = 'REFUSE';
    this.updateRdv(rdv);
  }

  updateRdv(rdv: RendezVous) {
    this.rdvService.update(rdv.id!, rdv).subscribe({
      next: () => this.loadRendezVous(),
      error: (err) => console.error('Erreur maj RDV', err)
    });

  }
  selectedRdv: RendezVous | null = null;
  showEditPanel: boolean = false;

  editRdv(rdv: RendezVous) {
    this.selectedRdv = { ...rdv }; // clone pour éviter de modifier directement
    this.showEditPanel = true;
  }

  cancelEdit() {
    this.selectedRdv = null;
    this.showEditPanel = false;
  }

  saveRdv() {
    if (!this.selectedRdv) return;

    this.rdvService.update(this.selectedRdv.id!, this.selectedRdv).subscribe({
      next: () => {
        this.loadRendezVous();
        this.cancelEdit();
      },
      error: (err) => console.error('Erreur sauvegarde RDV', err)
    });
  }
  selectedRdvForAvis: RendezVous | null = null;
  showAvisPanel: boolean = false;

  openAvisPanel(rdv: RendezVous) {
    this.selectedRdvForAvis = rdv;
    this.showAvisPanel = true;
    console.log("shg",this.selectedRdvForAvis);
  }

  closeAvisPanel() {
    this.selectedRdvForAvis = null;
    this.showAvisPanel = false;
  }
}
