export interface Avis {
  id: number;
  commentaire: string;
  note: number;                 // entre 1 et 5
  dateCreation: string;        // format ISO (ex: '2025-08-01T14:30:00')
  utilisateurId: number;       // ID de l'utilisateur
  utilisateurNom: string;       // ID de l'utilisateur
  vehiculeId: number;          // ID du véhicule
  vehiculeMarque: string;
  vehiculeModele: string;
}
