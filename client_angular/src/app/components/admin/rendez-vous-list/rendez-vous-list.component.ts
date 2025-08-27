import { Component, OnInit } from '@angular/core';
import { RendezVous } from '../../../models/rendezVous';
import { RendezVousService } from '../../../services/rendezVous.service';
import {UtilisateurService} from "../../../services/utilisateur.service";
import {Utilisateur} from "../../../models/utilisateur";
import {MagasinService} from "../../../services/magasin.service";

@Component({
  selector: 'app-rendez-vous-list',
  templateUrl: './rendez-vous-list.component.html',
  styleUrl: './rendez-vous-list.component.css'
})
export class RendezVousListComponent implements OnInit {
  rendezVousList: RendezVous[] = [];
  searchClient: string = '';
  searchVehicule: string = '';
  searchCommercial: string = '';
  searchDate: string = '';
  startDate: string = '';
  endDate: string = '';
  selectedRdv: RendezVous | null = null;
  showEditPanel: boolean = false;


  constructor(private rdvService: RendezVousService ,  private userService: UtilisateurService , private magasinService: MagasinService ) {
  }
  ngOnInit(): void {
    this.loadRendezVous();
    this.fetchUsers();
    this.getMagasins();
    console.log('hi');
  }

  loadRendezVous() {
    this.rdvService.getAll().subscribe({
      next: (data) => {
        this.rendezVousList = data.filter(rdv => rdv.statut === 'EN_ATTENTE');
        console.log('kk',this.rendezVousList);
      },


      error: (err) => console.error('Erreur chargement RDV', err)
    });
  }

  onFilterChange() {}

  get filteredRendezVousList(): RendezVous[] {
    return this.rendezVousList.filter(rdv => {
      const rdvDate = rdv.date ? new Date(rdv.date) : null;
      const start = this.startDate ? new Date(this.startDate) : null;
      const end = this.endDate ? new Date(this.endDate) : null;

      const matchesClient = !this.searchClient || rdv.clientFullName?.toLowerCase().includes(this.searchClient.toLowerCase());
      const matchesVehicule = !this.searchVehicule || rdv.vehiculeMakeModel?.toLowerCase().includes(this.searchVehicule.toLowerCase());
      const matchesCommercial = !this.searchCommercial || rdv.commercialFullName?.toLowerCase().includes(this.searchCommercial.toLowerCase());

      let matchesDate = true;
      if (rdvDate && start) matchesDate = rdvDate >= start;
      if (rdvDate && end) matchesDate = matchesDate && rdvDate <= end;

      return matchesClient && matchesVehicule && matchesCommercial && matchesDate;
    });
  }



  updateRdv(rdv: RendezVous) {
    this.rdvService.update(rdv.id!, rdv).subscribe({
      next: () => this.loadRendezVous(),
      error: (err) => console.error('Erreur maj RDV', err)
    });
  }

  editRdv(rdv: RendezVous) {
    this.selectedRdv = { ...rdv };
    console.log("f", this.selectedRdv);
    this.showEditPanel = true;
  }

  cancelEdit() {
    this.selectedRdv = null;
    this.showEditPanel = false;
  }

  validerRdv(rdv: RendezVous) {
    if (confirm("Voulez-vous vraiment valider ce rendez-vous ?")) {
      rdv.statut = 'VALIDE';
      this.updateRdv(rdv);
    }
  }

  refuserRdv(rdv: RendezVous) {
    if (confirm("Voulez-vous vraiment refuser ce rendez-vous ?")) {
      rdv.statut = 'REFUSE';
      this.updateRdv(rdv);
    }
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

  users: Utilisateur[] = [];
  fetchUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data.filter(user => user.role === 'COMMERCIAL');


      },
      error: (err) => {
        console.error('Error fetching users:', err);

      }
    });
  }
  magasins: any[] = [];

  getMagasins(): void {
    this.magasinService.getMagasins().subscribe(data => {
      this.magasins = data;

    });
  }
}
