export interface Piece {
  id: number; // facultatif (généré automatiquement par le backend)
  nom: string;
  reference: string;
  marque: string;
  prix: number;
  quantite: number;
  imageUrl: string;
  type: 'Frein' | 'Moteur' | 'Éclairage' | 'Filtre' | 'Pneu' | 'Accessoire';
  dateAchat: string; // ou Date si tu veux le type Date
  quantiteMinimum: number;
  description: string;
  compatibilite: string;
  precommandable: boolean;
  active: boolean;
  magasinId: number;
  blocId: number;
  magasinNom: string;
  blocNom: string;

}
