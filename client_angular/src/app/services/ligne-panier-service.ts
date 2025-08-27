import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from "../../environments/environment";
import {LignePanier} from "../models/ligne-panier";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class LignePanierService {

  private apiUrl = `http://localhost:8080/api/ligne-paniers`;

  constructor(private http: HttpClient) { }

  addLigneToPanier(panierId: number, ligne: LignePanier): Observable<LignePanier> {
    return this.http.post<LignePanier>(`${this.apiUrl}?panierId=${panierId}`, ligne);
  }

  removeLigne(ligneId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${ligneId}`);
  }

  updateLigne(ligneId: number, ligne: LignePanier): Observable<LignePanier> {
    return this.http.put<LignePanier>(`${this.apiUrl}/${ligneId}`, ligne);
  }
}
