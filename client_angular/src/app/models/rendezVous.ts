export interface RendezVous {
  id?: number;

  date: string;        // format 'YYYY-MM-DD'
  heure: string;       // format 'HH:mm'

  clientId?: number;
  clientFullName?: string;

  commercialId?: number;
  commercialFullName?: string;

  vehiculeId?: number;
  magasinId?: number;
  vehiculeMakeModel?: string;
  magasinNom?: string;
  avisCommentaire? : string;
   avisNote?: number;

  statut: 'EN_ATTENTE' | 'VALIDE' | 'REFUSE' | 'TERMINE';
}
