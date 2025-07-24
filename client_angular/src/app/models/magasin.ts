import {Bloc} from "./bloc";


export interface Magasin {
  id: number;
  nom: string;
  adresse: string;
  actif: boolean;

  blocs: Bloc[]; // liste des blocs appartenant à ce magasin
}
