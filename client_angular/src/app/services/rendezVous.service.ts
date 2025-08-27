import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RendezVous } from '../models/rendezVous';

@Injectable({
  providedIn: 'root'
})
export class RendezVousService {
  private apiUrl = 'http://localhost:8080/api/rendezvous';

  constructor(private http: HttpClient) {}

  /**
   * Récupérer tous les rendez-vous
   */
  getAll(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(this.apiUrl);
  }

  /**
   * Créer un nouveau rendez-vous
   */
  create(rdv: RendezVous): Observable<RendezVous> {
    return this.http.post<RendezVous>(this.apiUrl, rdv);
  }

  /**
   * Mettre à jour un rendez-vous
   */
  update(id: number, rdv: RendezVous): Observable<RendezVous> {
    return this.http.put<RendezVous>(`${this.apiUrl}/${id}`, rdv);
  }

  /**
   * Supprimer un rendez-vous
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Récupérer l'historique des rendez-vous (exclut ceux en attente côté backend)
   */
  getHistoriqueRendezVous(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.apiUrl}/historique`);
  }

  /**
   * Récupérer un rendez-vous par ID
   */
  getById(id: number): Observable<RendezVous> {
    return this.http.get<RendezVous>(`${this.apiUrl}/${id}`);
  }

  /**
   * Créer un rendez-vous avec date, heure, client et magasin
   */
  createWithParams(date: string, heure: string, clientId: number, magasinId: number, vehiculeId: number): Observable<RendezVous> {
    const formData = new FormData();
    formData.append('date', date);           // format "YYYY-MM-DD"
    formData.append('heure', heure);         // format "HH:mm"
    formData.append('clientId', clientId.toString());
    formData.append('magasinId', magasinId.toString());
    formData.append('vehiculeId', vehiculeId.toString());

    return this.http.post<RendezVous>(`${this.apiUrl}/add`, formData);
  }

  updateWithParams(
    id: number,
    date?: string,
    heure?: string,
    clientId?: number,
    magasinId?: number,
    vehiculeId?: number,
    statut?: string
  ): Observable<RendezVous> {
    const formData = new FormData();

    if (date) formData.append('date', date);               // format "YYYY-MM-DD"
    if (heure) formData.append('heure', heure);            // format "HH:mm"
    if (clientId) formData.append('clientId', clientId.toString());
    if (magasinId) formData.append('magasinId', magasinId.toString());
    if (vehiculeId) formData.append('vehiculeId', vehiculeId.toString());
    if (statut) formData.append('statut', statut.toUpperCase());

    return this.http.put<RendezVous>(`${this.apiUrl}/update/${id}`, formData);
  }


  // ✅ Récupérer tous les rendez-vous d’un client
  getRendezVousByClientId(clientId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.apiUrl}/client/${clientId}`);
  }


  cancelRdv(id: number): Observable<RendezVous> {
    return this.http.put<RendezVous>(`${this.apiUrl}/cancel/${id}`, {});
  }
}
