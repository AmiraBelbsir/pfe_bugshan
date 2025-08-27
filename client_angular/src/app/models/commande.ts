import {LigneCommande} from "./ligne-commande";

export interface Commande {
  id?: number;
  utilisateurId: number;
  utilisateurNom?: string;
  lignes: LigneCommande[];
  total: number;
  statut?: string;          // "EN_ATTENTE", "EN_COURS", "LIVREE", etc.
  dateCommande?: string;    // ISO string
}
