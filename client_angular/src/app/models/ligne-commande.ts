// ligne-commande.model.ts
export interface LigneCommande {
  id?: number;
  pieceId: number;
  pieceNom?: string;
  prixUnitaire?: number;
  quantite: number;
  livraison?: string; // 'standard' | 'express'
  total?: number;
}
