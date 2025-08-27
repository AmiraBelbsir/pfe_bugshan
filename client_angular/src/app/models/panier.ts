import {LignePanier} from "./ligne-panier";
import {Utilisateur} from "./utilisateur";

export interface Panier {
  id?: number;                  // optionnel : null à la création
  confirme: boolean;             // panier confirmé ou non

  utilisateurId: number;         // id de l'utilisateur
  utilisateurNom?: string;       // nom de l'utilisateur (optionnel, utile à l'affichage)

  lignes: LignePanier[];     // liste des lignes dans le panier
  total?: number;                // total calculé par le back

  // Pour ajouter une seule ligne (comme dans ton DTO backend)
  ligne: LignePanier;
}
