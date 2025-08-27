import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PieceService } from "../../../services/piece.service";
import { PanierService} from "../../../services/panier.service";
import { Piece } from "../../../models/piece";
import { environment } from "../../../../environments/environment";
import {LignePanier} from "../../../models/ligne-panier";
import {Panier} from "../../../models/panier";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-ajouter-au-panier',
  templateUrl: './ajouter-au-panier.component.html',
  styleUrls: ['./ajouter-au-panier.component.css']
})
export class AjouterAuPanierComponent implements OnInit {
  piece: Piece | null = null;
  quantity = 1;
  deliveryOption = 'standard'; // 'standard' or 'express'
  panierId?: number; // pour garder trace d'un panier existant
  user: any;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pieceService: PieceService,
    private panierService: PanierService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const pieceId = this.route.snapshot.paramMap.get('id');
    if (pieceId) {
      this.loadPiece(pieceId);
    }
    const currentUser = this.authService.getUser();
    this.user.id=currentUser.id;
  }

  loadPiece(id: string): void {
    this.pieceService.getPieceById(+id).subscribe({
      next: (data) => {
        this.piece = data;
      },
      error: (err) => {
        console.error('Erreur de chargement de la pièce', err);
        this.router.navigate(['/pieces']);
      }
    });
  }

  incrementQuantity(): void {
    if (this.piece && this.quantity < this.piece.quantite) {
      this.quantity++;
    }
  }

  decrementQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  calculateTotal(): number {
    if (!this.piece) return 0;
    const deliveryCost = this.deliveryOption === 'express' ? 120 : 50;
    return (this.piece.prix * this.quantity) + deliveryCost;
  }

  addToCart(): void {
    if (!this.piece) return;

    // Créer la ligne panier DTO
    const ligne: LignePanier = {
      prixUnitaire: 0,
      pieceId: this.piece.id!,
      quantite: this.quantity,
      livraison: this.deliveryOption
    };

    // Créer le panier DTO
    const panierDTO: Panier = {
      lignes: [],
      confirme: false,
      id: this.panierId,        // undefined si c'est un nouveau panier
      utilisateurId: this.user.id,         // ici mettre l'id de l'utilisateur connecté
      ligne: ligne
    };

    // Appel au service pour ajouter la pièce
    this.panierService.addPieceToPanier(panierDTO).subscribe({
      next: (res) => {
        console.log('Pièce ajoutée au panier', res);
        // garder l'id du panier pour les ajouts suivants
        this.panierId = res.id;
        // redirection vers la page panier
        this.router.navigate(['/panier']);
      },
      error: (err) => {
        console.error('Erreur ajout panier', err);
      }
    });
  }

  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }
}
