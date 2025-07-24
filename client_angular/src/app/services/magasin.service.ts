import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Magasin} from "../models/magasin";
import { Bloc } from '../models/bloc';




@Injectable({
  providedIn: 'root'
})
export class MagasinService {

  private apiUrl = 'http://localhost:8080/api/magasins';

  constructor(private http: HttpClient) {}

  // Méthode pour récupérer tous les magasins
  getMagasins(): Observable<Magasin[]> {
    return this.http.get<Magasin[]>(this.apiUrl);
  }

  // Méthode  pour récupérer un magasin par ID
  getMagasinById(id: number): Observable<Magasin> {
    return this.http.get<Magasin>(`${this.apiUrl}/${id}`);
  }

  updateMagasin(magId: number, magasinData: { nom: string, adresse: string,  actif: boolean }): Observable<any> {
    const formData = new FormData()
    formData.append('nom', magasinData.nom);
     formData.append('adresse', magasinData.adresse);
    formData.append('actif', magasinData.actif.toString());  // Convert boolean to string for the form data


    return this.http.put<any>(`${this.apiUrl}/${magId}`, formData);
  }



  // 🟢 Ajouter un magasin

  addMagasin(magasinData: {
    nom: string;
    adresse: string;
  }): Observable<any> {
    const formData = new FormData();
    formData.append('nom', magasinData.nom);
    formData.append('adresse', magasinData.adresse);

    return this.http.post<any>(this.apiUrl, formData);
  }

  // ➕ Ajouter un bloc à un magasin
  addBlocToMagasin(magasinId: number, blocData: { nom: string; description: string }): Observable<any> {
    const formData = new FormData();
    formData.append('nom', blocData.nom);
    formData.append('description', blocData.description);

    return this.http.post<any>(`http://localhost:8080/api/magasins/${magasinId}/blocs`, formData);
  }


}
