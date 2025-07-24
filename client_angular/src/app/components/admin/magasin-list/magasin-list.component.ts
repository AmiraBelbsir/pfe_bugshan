import { Component, OnInit } from '@angular/core';
import { Magasin } from '../../../models/magasin';
import { MagasinService } from '../../../services/magasin.service';
import {Bloc} from "../../../models/bloc";
import {BlocService} from "../../../services/bloc.service";

@Component({
  selector: 'app-magasin-list',
  templateUrl: './magasin-list.component.html',
  styleUrls: ['./magasin-list.component.css']
})

export class MagasinListComponent implements OnInit {
  magasins: Magasin[] = [];            // Tous les magasins récupérés du backend
  blocs: Bloc[] = [];            // Tous les magasins récupérés du backend
  filteredMagasins: Magasin[] = [];    // Magasins filtrés à afficher
  searchTerm: string = '';             // Terme de recherche

  constructor(private magasinService: MagasinService, private blocService: BlocService) {}
  selectedMagasin: any = null; // magasin sélectionné
  showBlocPanel: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  magasinAdded: boolean = false;
  showEditPanel: boolean = false;
  showAddPanel: boolean = false;

  selectedBloc: Bloc | null = null;

  openPopup(bloc: Bloc): void {
    this.selectedBloc = bloc;
  }

  closePopup(): void {
    this.selectedBloc = null;
  }


  openBlocPanel(magasin: any): void {
    this.selectedMagasin = magasin;
    this.blocService.getBlocsByMagasinId(this.selectedMagasin.id).subscribe({
      next: (data) => {

          this.blocs = data;

      },});
    this.showBlocPanel = true;
  }

  togglePanel(): void {
    this.showAddPanel = !this.showAddPanel;
    this.resetNewMagasin();
  }


  resetNewMagasin(): void {
    this.newMagasin = {
      nom: "",
      adresse: "",
    }
  }
// Method to close the panel
  closeAddPanel(): void {
    this.showEditPanel = false;
    this.resetNewMagasin();
  }
  closeBlocPanel(): void {
    this.showBlocPanel = false;
    this.selectedMagasin = null;
    this.blocs = [];
  }

  ngOnInit(): void {
    this.getMagasins();
  }

  getMagasins(): void {
    this.magasinService.getMagasins().subscribe(data => {
      this.magasins = data;
      this.filteredMagasins = data;
      console.log('jj', this.magasins);
    });
  }

  filterMagasins(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredMagasins = this.magasins.filter(m =>
      m.nom.toLowerCase().includes(term)
    );
  }


  toggleActivation(magasin: Magasin): void {
    const nouveauStatut = !magasin.actif;

  }
  // Nouveau bloc à créer
  newBloc: { nom: string; description: string } = {
    nom: '',
    description: ''
  };

  showAddBlocForm: boolean = false;

  addBlocToMagasin(): void {
    // Vérifie que tout est rempli
    if (!this.selectedMagasin || !this.newBloc.nom || !this.newBloc.description) {
      this.errorMessage = 'Veuillez remplir tous les champs du bloc.';
      return;
    }

    // Appelle le service pour ajouter le bloc
    this.magasinService.addBlocToMagasin(this.selectedMagasin.id, {
      nom: this.newBloc.nom,
      description: this.newBloc.description
    }).subscribe({
      next: (bloc) => {
        console.log("Bloc ajouté :", bloc);

        // Ajoute le nouveau bloc à la liste affichée
        this.blocs.push(bloc);

        // Réinitialise le formulaire de saisie
        this.newBloc = { nom: '', description: '' };

        // Message de succès
        this.successMessage = "Bloc ajouté avec succès !";

        // Optionnel : cache le formulaire d'ajout si tu veux
        this.showAddBlocForm = false;

        // Efface le message après 3 secondes
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        console.error("Erreur lors de l'ajout du bloc :", err);

        // Affiche un message d'erreur
        this.errorMessage = "Erreur lors de l'ajout du bloc.";
      }
    });
  }


  // Nouveau magasin à créer
  newMagasin: { nom: string; adresse: string } = {
    nom: '',
    adresse: '',


  };

  addMagasin(): void {
    if (!this.newMagasin.nom || !this.newMagasin.adresse) {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }
    this.magasinService.addMagasin(this.newMagasin).subscribe({
      next: (magasin) => {
        this.magasins.push(magasin);
        this.filterMagasins();
        this.successMessage = 'Magasin ajouté avec succès';
        this.magasinAdded = true;
        this.closeAddPanel();
        this.showAddPanel = false;

        setTimeout(() => this.magasinAdded = false, 3000);
      },
      error: (err) => {
        console.error('Erreur ajout magasin:', err);
        this.errorMessage = 'Erreur lors de l\'ajout du magasin.';
      }
    });
  }
// 👇 Variable pour stocker le magasin sélectionné pour modification
  selectedMagasinToEdit: Magasin | null = null;



// 👇 Méthode appelée quand l'admin clique sur "Modifier"
  editMagasin(magasin: Magasin): void {
    // On clone l'objet pour éviter de modifier directement la liste
    this.selectedMagasinToEdit = { ...magasin };
    this.showEditPanel = true; // Affiche le formulaire
  }

// 👇 Méthode pour enregistrer les modifications
  updateMagasin(): void {
    if (!this.selectedMagasinToEdit) return; // Sécurité

    this.magasinService.updateMagasin(this.selectedMagasinToEdit.id, this.selectedMagasinToEdit).subscribe({
      next: (updatedMagasin) => {
        // Mise à jour de la liste locale
        const index = this.magasins.findIndex(m => m.id === updatedMagasin.id);
        if (index !== -1) {
          this.magasins[index] = updatedMagasin;
          this.filterMagasins(); // Met à jour la liste affichée
        }

        // Réinitialisation du formulaire
        this.selectedMagasinToEdit = null;
        this.showEditPanel = false;
      },
      error: (err) => {
        console.error('Erreur de mise à jour du magasin :', err);
        // Tu peux ici afficher un message d'erreur à l'utilisateur
      }
    });
  }

// 👇 Méthode pour annuler la modification
  cancelEdit(): void {
    this.selectedMagasinToEdit = null;
    this.showEditPanel = false;
  }
  toggleAddBlocForm(): void {
    this.showAddBlocForm = !this.showAddBlocForm;
    if (!this.showAddBlocForm) {
      this.resetNewBloc();
    }
  }

  resetNewBloc(): void {
    this.newBloc = { nom: '', description: '' };
  }

  cancelAddBloc(): void {
    this.showAddBlocForm = false;
    this.resetNewBloc();
  }

  addBloc(): void {
    if (!this.selectedMagasin) {
      console.error("Aucun magasin sélectionné");
      return;
    }
    this.magasinService.addBlocToMagasin(this.selectedMagasin.id, this.newBloc).subscribe({
      next: (bloc) => {
        this.blocs.push(bloc);  // Ajoute le nouveau bloc à la liste
        this.cancelAddBloc();
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout du bloc:', err);
        // Afficher message d'erreur utilisateur si besoin
      }

    });

  }

}
