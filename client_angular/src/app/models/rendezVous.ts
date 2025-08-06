export interface RendezVous {
  id?: number;
  date: string;        // format 'YYYY-MM-DD'
  heure: string;       // format 'HH:mm'
  clientNom: string;
  vehiculeNom: string;
  commercialId?: number;
  commercialNom?: string;
  statut: 'EN_ATTENTE' | 'VALIDE' | 'REFUSE' | 'TERMINE';
}
