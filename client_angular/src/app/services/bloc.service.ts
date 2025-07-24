import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bloc } from '../models/bloc';


@Injectable({
  providedIn: 'root'
})
export class BlocService {
  private apiUrl = 'http://localhost:8080/api/blocs';

  constructor(private http: HttpClient) {}

  // Obtenir tous les blocs
  getAllBlocs(): Observable<Bloc[]> {
    return this.http.get<Bloc[]>(this.apiUrl);
  }

  // Obtenir un bloc par ID
  getBlocById(id: number): Observable<Bloc> {
    return this.http.get<Bloc>(`${this.apiUrl}/${id}`);
  }

  // Obtenir les blocs d’un magasin spécifique
  getBlocsByMagasinId(magasinId: number): Observable<Bloc[]> {
    return this.http.get<Bloc[]>(`${this.apiUrl}/magasin/${magasinId}`);
  }

  // ✅ Ajouter un bloc à un magasin
  addBlocToMagasin(magasinId: number, bloc: { nom: string; description: string }): Observable<Bloc> {
    const params = new URLSearchParams();
    params.set('nom', bloc.nom);
    params.set('description', bloc.description);

    return this.http.post<Bloc>(`${this.apiUrl}/${magasinId}/blocs?${params.toString()}`, {});
  }
}
