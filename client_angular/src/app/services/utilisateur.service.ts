import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Utilisateur } from '../models/utilisateur';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {
  }

  updateField(userId: number, field: keyof Utilisateur, value: any): Observable<Utilisateur> {
    return this.http.patch<Utilisateur>(`${this.apiUrl}/${userId}`, { field, value });
  }

  deactivateUser(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/deactivate/${id}`, {});
  }

  getAllUsers(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(`${this.apiUrl}/${id}`);
  }

  createUtilisateurAvecImage(utilisateurData: {
    nomComplet: string,
    email: string,
    nomUtilisateur: string,
    motDePasse: string,
    numeroTelephone: string,
    sexe: string,
    role: string,
    ville: string,
    adresse: string
  }, fichier?: File): Observable<any> {
    const formData = new FormData();

    if (fichier) {
      formData.append('fichier', fichier);
    }

    formData.append('nomComplet', utilisateurData.nomComplet);
    formData.append('email', utilisateurData.email);
    formData.append('nomUtilisateur', utilisateurData.nomUtilisateur);
    formData.append('motDePasse', utilisateurData.motDePasse);
    formData.append('numeroTelephone', utilisateurData.numeroTelephone);
    formData.append('sexe', utilisateurData.sexe.toUpperCase());
    formData.append('role', utilisateurData.role.toUpperCase());
    formData.append('ville', utilisateurData.ville.toUpperCase());
    formData.append('adresse', utilisateurData.adresse);

    return this.http.post<any>(this.apiUrl, formData);
  }

  updateUtilisateur(utilisateurId: number, utilisateurData: {
    nomComplet: string,
    email: string,
    nomUtilisateur: string,
    numeroTelephone: string,
    sexe: string,
    role: string,
    ville: string,
    adresse: string,
    actif: boolean,
    motDePasse?: string
  }, fichierImage?: File): Observable<any> {
    const formData = new FormData();

    formData.append('nom_complet', utilisateurData.nomComplet);
    formData.append('email', utilisateurData.email);
    formData.append('nomUtilisateur', utilisateurData.nomUtilisateur);
    formData.append('numero_telephone', utilisateurData.numeroTelephone);
    formData.append('sexe', utilisateurData.sexe.toUpperCase());
    formData.append('role', utilisateurData.role.toUpperCase());
    formData.append('ville', utilisateurData.ville.toUpperCase());
    formData.append('adresse', utilisateurData.adresse);
    formData.append('actif', utilisateurData.actif.toString());

    if (utilisateurData.motDePasse) {
      formData.append('mot_de_passe', utilisateurData.motDePasse);
    }

    if (fichierImage) {
      formData.append('fichier', fichierImage);
    }

    return this.http.put<any>(`${this.apiUrl}/${utilisateurId}`, formData);
  }
}
