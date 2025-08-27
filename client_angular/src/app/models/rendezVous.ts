import {Avis} from "./avis";

export interface RendezVous {
  id: number;

  date: string;        // format 'YYYY-MM-DD'
  heure: string;       // format 'HH:mm'

  clientId?: number;
  clientFullName?: string;

  commercialId?: number;
  commercialFullName?: string;
  commercialImage?: string;

  vehiculeId?: number;
  magasinId?: number;
  vehiculeMakeModel?: string;
  vehiculeImage?: string;
  magasinNom?: string;
  magasinAdresse?: string;
  avisCommentaire? : string;
  avisNote?: number;
avis: Avis;
  statut: 'EN_ATTENTE' | 'VALIDE' | 'REFUSE' | 'TERMINE';
}

