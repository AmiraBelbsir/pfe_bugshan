import {Component, OnInit} from '@angular/core';
import {Panier} from "../../../models/panier";
import {Router} from "@angular/router";
import {LignePanier} from "../../../models/ligne-panier";
import {Piece} from "../../../models/piece";
import {PieceService} from "../../../services/piece.service";
import {PanierService} from "../../../services/panier.service";
import {AuthService} from "../../../services/auth.service";
import {environment} from "../../../../environments/environment";
import {CodePromoService} from "../../../services/code-promo.service";
import {LigneCommande} from "../../../models/ligne-commande";

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrl: './panier.component.css'
})
export class PanierComponent implements OnInit {
  panier: Panier = {
    id: 0, ligne: {} as LignePanier, total: 0, utilisateurNom: "",
    confirme: false,
    utilisateurId: 1, // This should come from authentication
    lignes: []
  };
  total: number = 0; // total de la commande confirmée
  lignesCommande: LigneCommande[] = []; // lignes de commande confirmée
  piecesDetails: Map<number, Piece> = new Map();
  promoCode: string = '';
  promoApplied: boolean = false;
  promoError: string = '';
  discountAmount: number = 0;
  newTotal: number = 0;


  constructor(
    private router: Router,
    private panierService: PanierService,
    private pieceService: PieceService,
    private authService: AuthService,
    private codePromoService:CodePromoService
  ) { }

  ngOnInit(): void {
    this.loadPanier();
  }

  loadPanier(): void {
    const utilisateurId = this.authService.getUser().id;

    this.panierService.getPanierByUtilisateur(utilisateurId).subscribe({
      next: (data) => {
        this.panier = data;
        console.log('Panier chargé:', this.panier);
      },
      error: (err) => {
        console.error('Erreur lors du chargement du panier', err);
        this.panier = {
          ligne: {} as LignePanier,
          utilisateurId,
          confirme: false,
          lignes: []
        }; // fallback : panier vide
      }
    });
  }

  loadPiecesDetails(): void {
    if (!this.panier.lignes) return;

    // Load details for each piece in the panier
    this.panier.lignes.forEach(ligne => {
      this.pieceService.getPieceById(ligne.pieceId).subscribe({
        next: (piece) => {
          this.piecesDetails.set(ligne.pieceId, piece);
        },
        error: (err) => {
          console.error('Error loading piece details', err);
        }
      });
    });
  }

  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }

  getPieceReference(pieceId: number): string {
    const piece = this.piecesDetails.get(pieceId);
    return piece?.reference || '';
  }

  updateQuantity(ligne: LignePanier, newQuantity: number): void {
    if (newQuantity < 1) return;

    // update local value before call
    ligne.quantite = newQuantity;

    this.panierService.updateLignePanier(this.panier.id!, ligne.id!, ligne).subscribe({
      next: (updatedLigne) => {
        this.total = this.calculateTotal();
        // si la ligne existe déjà → on la met à jour
        const index = this.panier.lignes.findIndex(l => l.id === updatedLigne.id);
        if (index !== -1) {
          this.panier.lignes[index] = updatedLigne;
        } else {
          // sinon → on la push (au cas où c’est une nouvelle ligne)
          this.panier.lignes.push(updatedLigne);
        }
        window.location.reload();
      },
      error: (err) => {
        console.error('Error updating quantity', err);
      }
    });
  }


  removeLigne(ligne: LignePanier): void {
    if (!this.panier.id || !ligne.id) return;

    this.panierService.removeLigneFromPanier(this.panier.id, ligne.id).subscribe({
      next: (updatedPanier) => {
        this.panier = updatedPanier;
      },
      error: (err) => {
        console.error('Error removing ligne', err);
      }
    });
  }


  confirmPanier(): void {
    if (!this.panier.id) return;

    this.panierService.confirmPanier(this.panier.id).subscribe({
      next: (commande) => {
        console.log('Commande confirmée :', commande);

        // Stocker la commande pour le checkout
        this.router.navigate(['/checkout'], { state: { commande } });

        // Mettre à jour les totaux locaux si nécessaire
        this.total = commande.total;
        this.lignesCommande = commande.lignes;
      },
      error: (err) => console.error('Error confirming panier', err)
    });
  }


  applyPromoCode(): void {
    if (!this.panier?.id) return;

    const request = {
      panierId: this.panier.id,
      utilisateurId: this.panier.utilisateurId, // Assure-toi que tu as l'ID de l'utilisateur
      code: this.promoCode
    };

    this.codePromoService.applyPromo(request).subscribe({
      next: (response) => {
        this.panier = response.panier;        // panier mis à jour côté backend
        this.discountAmount = response.discountAmount;
        this.newTotal = response.newTotal;
        this.promoApplied = true;
        this.promoError = '';
      },
      error: (err) => {
        console.error('Erreur code promo', err);
        this.promoError = 'Code promo invalide ou non applicable';
        this.promoApplied = false;
      }
    });
  }

  calculateSubtotal(): number {
    return this.panier?.lignes?.reduce((total, ligne) => {
      return total + (ligne.prixUnitaire * ligne.quantite);
    }, 0) ?? 0;
  }

  calculateDeliveryCost(): number {
    return this.panier?.lignes?.reduce((total, ligne) => {
      const frais = ligne.livraison?.toLowerCase() === 'express' ? 120 : 50;
      return total + frais;
    }, 0) ?? 0;
  }

  calculateTotal(): number {
    const subtotal = this.calculateSubtotal();
    const delivery = this.calculateDeliveryCost();
    const discount = this.discountAmount || 0;
    return subtotal + delivery - discount;
  }


}
