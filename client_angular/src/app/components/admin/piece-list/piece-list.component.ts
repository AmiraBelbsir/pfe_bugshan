import { Component, OnInit } from '@angular/core';
import { Piece } from "../../../models/piece";
import { PieceService } from "../../../services/piece.service";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { environment } from "../../../../environments/environment";
import { MagasinService } from "../../../services/magasin.service";
import { Magasin } from "../../../models/magasin";
import { BlocService } from "../../../services/bloc.service";

@Component({
  selector: 'app-piece-list',
  templateUrl: './piece-list.component.html',
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule, NgClass],
  styleUrls: ['./piece-list.component.css']
})
export class PieceListComponent implements OnInit {
  magasins: Magasin[] = [];
  blocs: any[] = [];
  pieces: Piece[] = [];
  filteredPieces: Piece[] = [];

  searchTerm: string = '';
  selectedType: string = '';
  startDate: string = '';
  endDate: string = '';
  isSortedAZ: boolean = true;

  showAddPanel: boolean = false;
  showEditPanel: boolean = false;
  isEditing: boolean = false;

  selectedFile?: File;
  selectedPiece?: Piece;

  errorMessage: string = '';
  successMessage: string = '';
  imageError: string = '';

  newPiece: {
    id: number;
    nom: string;
    marque: string;
    description: string;
    prix: number;
    quantite: number;
    quantiteMinimum: number;
    dateAchat: string;
    type: string;
    reference: string;
    precommandable: boolean;
    imageUrl: string;
    magasinId: number;
    blocId: number ;
    magasinNom: string;
    blocNom: string;
    fournisseurId?: number ;
    vehiculeIds?: number[];
  } = {
    id: 0,
    nom: '',
    marque: '',
    description: '',
    prix: 0,
    quantite: 0,
    quantiteMinimum: 0,
    dateAchat: '',
    type: 'Frein',
    reference: '',
    precommandable: false,
    imageUrl: '',
    magasinId: 0,
    blocId: 0,
    magasinNom: '',
    blocNom: '',
    fournisseurId: 0,
    vehiculeIds: []
  };

  constructor(
    private pieceService: PieceService,
    private magasinService: MagasinService,
    private blocService: BlocService
  ) {}

  ngOnInit(): void {
    this.loadPieces();
    this.loadMagasins();
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }
  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }


  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }

  loadPieces(): void {
    this.pieceService.getPieces().subscribe({
      next: (data) => {
        this.pieces = data;
        this.filteredPieces = data;
      },
      error: (err) => {
        console.error('Erreur de chargement des pièces', err);
      }
    });
  }

  loadMagasins(): void {
    this.magasinService.getMagasins().subscribe({
      next: (data) => {
        this.magasins = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des magasins :', err);
      }
    });
  }

  onMagasinChange(magasinId: number): void {
    if (!magasinId) {
      this.blocs = [];
      return;
    }

    this.blocService.getBlocsByMagasinId(magasinId).subscribe({
      next: (data) => {
        this.blocs = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des blocs pour le magasin sélectionné :', err);
      }
    });
  }

  togglePanel(): void {
    this.showAddPanel = !this.showAddPanel;
    this.resetForm();
  }

  resetForm(): void {
    this.newPiece = {
 fournisseurId: undefined, vehiculeIds: [],
      id: 0,
      nom: '',
      marque: '',
      description: '',
      prix: 0,
      quantite: 0,
      quantiteMinimum: 0,
      dateAchat: '',
      type: 'Frein',
      reference: '',
      precommandable: false,
      imageUrl: '',
      magasinId: 0,
      blocId: 0,
      magasinNom: '',
      blocNom: ''

    };
  }

  filterPieces(): void {
    this.filteredPieces = this.pieces
      .filter(piece => {
        const matchesName = !this.searchTerm || piece.nom.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesType = !this.selectedType || piece.type === this.selectedType;
        const matchesDateRange =
          (!this.startDate || piece.dateAchat >= this.startDate) &&
          (!this.endDate || piece.dateAchat <= this.endDate);
        return matchesName && matchesType && matchesDateRange;
      })
      .sort((a, b) =>
        this.isSortedAZ ? a.nom.localeCompare(b.nom) : b.nom.localeCompare(a.nom)
      );
  }

  sortByName(): void {
    this.isSortedAZ = !this.isSortedAZ;
    this.filterPieces();
  }
  viewDetails(pieceId: number): void {
    console.log('id:', pieceId);
    this.pieceService.getPieceById(pieceId).subscribe({
      next: (piece) => {
        this.selectedPiece = { ...piece }; // Clone pour éviter de modifier directement
        this.showEditPanel = true;
        this.isEditing = false; // Mode lecture par défaut
      },
      error: (err) => {
        console.error('Erreur lors du chargement des détails de la pièce :', err);
      }
    });
  }


  enableEditing(): void {
    this.isEditing = true;
  }
  addPiece(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.newPiece.nom || !this.newPiece.marque) {
      this.errorMessage = 'Le nom et la marque sont obligatoires';
      return;
    }

    if (this.newPiece.prix == null || this.newPiece.prix < 0) {
      this.errorMessage = 'Le prix est obligatoire et doit être positif';
      return;
    }

    if (this.newPiece.quantite == null || this.newPiece.quantite < 0) {
      this.errorMessage = 'La quantité est obligatoire et doit être positive';
      return;
    }

    if (this.selectedFile && !this.selectedFile.type.startsWith('image/')) {
      this.errorMessage = 'Le fichier doit être une image valide';
      return;
    }

    const pieceData = {
      nom: this.newPiece.nom,
      reference: this.newPiece.reference || '',
      marque: this.newPiece.marque,
      type: this.newPiece.type || '',
      prix: this.newPiece.prix,
      quantite: this.newPiece.quantite,
      quantiteMinimum: this.newPiece.quantiteMinimum || 0,
      dateAchat: this.newPiece.dateAchat,
      description: this.newPiece.description || '',
      precommandable: this.newPiece.precommandable || false,
      magasinId: this.newPiece.magasinId,
      blocId: this.newPiece.blocId,
      fournisseurId: this.newPiece.fournisseurId,
      vehiculeIds: this.newPiece.vehiculeIds || []
    };

    const fileToSend = this.selectedFile || undefined;

    this.pieceService.createPieceWithImage(pieceData, fileToSend).subscribe({
      next: (piece) => {
        this.successMessage = 'Pièce ajoutée avec succès !';
        this.pieces.push(piece);
        this.filteredPieces.push(piece);
        this.resetForm();
        this.showAddPanel = false;
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        console.error("Erreur lors de l'ajout de la pièce", err);
        this.errorMessage = "Erreur lors de l'ajout de la pièce.";
      }
    });
  }

  openEditPanel(piece: Piece): void {
    this.selectedPiece = { ...piece };
    this.showEditPanel = true;
    this.isEditing = true;
    this.selectedFile = undefined;
  }

  closePanel(): void {
    this.showEditPanel = false;
    this.isEditing = false;
    this.selectedPiece = undefined;
    this.errorMessage = '';
    this.successMessage = '';
    this.selectedFile = undefined;
  }

  pieceTypes: string[] = ['Frein', 'Moteur', 'Éclairage', 'Filtre', 'Pneu', 'Accessoire'];

  updatePiece(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.selectedPiece || this.selectedPiece.id === undefined) {
      this.errorMessage = 'Aucune pièce sélectionnée.';
      return;
    }

    if (!this.selectedPiece.nom || !this.selectedPiece.reference || !this.selectedPiece.marque || this.selectedPiece.prix === undefined || this.selectedPiece.quantite === undefined) {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }

    if (this.imageError) {
      this.errorMessage = this.imageError;
      return;
    }

    const fileToSend = this.selectedFile ? this.selectedFile : undefined;

    const pieceData = {
      nom: this.selectedPiece.nom,
      reference: this.selectedPiece.reference,
      marque: this.selectedPiece.marque,
      prix: this.selectedPiece.prix,
      quantite: this.selectedPiece.quantite,
      quantiteMinimum: this.selectedPiece.quantiteMinimum,
      description: this.selectedPiece.description,

      active: this.selectedPiece.active,
      type: this.selectedPiece.type,
      dateAchat: this.selectedPiece.dateAchat,
      precommandable: this.selectedPiece.precommandable,
      magasinId: this.selectedPiece.magasinId,
      blocId: this.selectedPiece.blocId,
      fournisseurId: this.selectedPiece.fournisseurId,     // AJOUTÉ
      vehiculeIds: this.selectedPiece.vehiculeIds || []    // AJOUTÉ
    };

    this.pieceService.updatePiece(this.selectedPiece.id, pieceData, fileToSend).subscribe({
      next: (updatedPiece) => {
        const index = this.pieces.findIndex(p => p.id === updatedPiece.id);
        if (index !== -1) {
          this.pieces[index] = updatedPiece;
          this.filterPieces();
        }

        this.successMessage = 'Pièce modifiée avec succès.';
        this.closePanel();

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        if (err.status === 404) {
          this.errorMessage = 'Pièce non trouvée.';
        } else if (err.status === 400) {
          this.errorMessage = 'Erreur dans les données envoyées.';
        } else {
          this.errorMessage = 'Erreur lors de la mise à jour.';
        }
      }
    });
  }
}
