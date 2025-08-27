export interface Utilisateur {
  id: number;
  nomComplet: string;
  numeroTelephone: string;
  motDePasse: string;
  email: string;
  role: string;
  sexe: string;
  urlImage: string;
  actif: boolean;
  nomUtilisateur: string;
  adresse: string;
  ville: string;
  dateInscription?: string;
}
