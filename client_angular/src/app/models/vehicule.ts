export interface Vehicule {
  id: number;
  urlImage: string;                // URL de l'image principale du véhicule
  marque: string;                  // Marque du véhicule
  modele: string;                  // Modèle du véhicule
  quantite: number;               // Quantité en stock
  annee: number;                   // Année de fabrication
  couleur: string;                 // Couleur du véhicule
  sieges: number;                  // Nombre de sièges
  prix: number;                    // Prix par jour
  disponible: boolean;             // Disponibilité du véhicule
  emplacement: string;             // Emplacement actuel
  niveauCarburant: number;         // Niveau de carburant
  typeVehicule: string;            // Type de véhicule (SEDAN, SUV, etc.)
  typeCarburant: string;           // Type de carburant (GAS, DIESEL, etc.)
  typeTransmission: string;        // Transmission (AUTOMATIC, MANUAL)
  photosAdditionnelles?: string[]; // URLs des photos supplémentaires
}
