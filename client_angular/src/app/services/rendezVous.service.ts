import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {RendezVous} from "../models/rendezVous";


@Injectable({
  providedIn: 'root'
})
export class RendezVousService {
  private apiUrl = 'http://localhost:8080/api/rendezvous';

  constructor(private http: HttpClient) {}

  getAll(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(this.apiUrl);
  }

  update(id: number, rdv: RendezVous): Observable<RendezVous> {
    return this.http.put<RendezVous>(`${this.apiUrl}/${id}`, rdv);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  getHistoriqueRendezVous(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.apiUrl}/historique`);
  }

}
