import {Panier} from "../models/panier";
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {LignePanier} from "../models/ligne-panier";
import {Commande} from "../models/commande";

@Injectable({
  providedIn: 'root'
})
export class PanierService {

  private apiUrl = `http://localhost:8080/api/paniers`;

  constructor(private http: HttpClient) { }

  addPieceToPanier(panierDTO: Panier): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, panierDTO);
  }

  getPanierByUtilisateur(utilisateurId: number): Observable<Panier> {
    return this.http.get<Panier>(`${this.apiUrl}/utilisateur/${utilisateurId}`);
  }

  updateLignePanier(panierId: number, ligneId: number, ligne: LignePanier): Observable<LignePanier> {
    return this.http.put<LignePanier>(`${this.apiUrl}/${panierId}/ligne/${ligneId}`, ligne);
  }

  // panier.service.ts
  removeLigneFromPanier(panierId: number, ligneId: number): Observable<Panier> {
    return this.http.delete<Panier>(`${this.apiUrl}/${panierId}/ligne/${ligneId}`);
  }

// panier.service.ts
  confirmPanier(panierId: number): Observable<Commande> {
    return this.http.put<Commande>(`${this.apiUrl}/${panierId}/confirm`, {});
  }



}
