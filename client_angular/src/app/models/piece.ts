export interface Piece {
  id: number;
  nom: string;
  reference: string;
  marque: string;
  prix: number;
  quantite: number;
  quantiteMinimum: number;
  imageUrl: string;
  type: 'Frein' | 'Moteur' | 'Éclairage' | 'Filtre' | 'Pneu' | 'Accessoire';
  dateAchat: string; // ISO string (ex: '2025-08-05')
  description: string;
  precommandable: boolean;
  active: boolean;

  // Magasin
  magasinId: number;
  magasinNom: string;

  // Bloc
  blocId: number;
  blocNom: string;

  // Fournisseur
  fournisseurId: number;
  fournisseurNom: string;

  // Véhicules compatibles (juste les IDs)
  vehiculeIds: number[];
}
