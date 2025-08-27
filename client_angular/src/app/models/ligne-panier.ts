import {Piece} from "./piece";

export interface LignePanier {
  id?: number;             // optionnel car peut être null à la création
  pieceId: number;         // id de la pièce
  pieceNom?: string;       // optionnel, utilisé pour afficher dans le front
  prixUnitaire: number;   // optionnel, rempli par le backend
  quantite: number;        // quantité demandée
  livraison: string;       // "standard" | "express"
  pieceImage?: string;       // "standard" | "express"
  total?: number;          // calculé côté back
}
