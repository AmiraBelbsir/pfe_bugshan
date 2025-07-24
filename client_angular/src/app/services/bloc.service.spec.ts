import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface Bloc (à adapter selon les champs de Bloc)
export interface Bloc {
  id: number;
  nom: string;
  // si tu veux, ajoute d'autres propriétés ici
}

@Injectable({
  providedIn: 'root'
})
export class BlocService {

  private baseUrl = 'http://localhost:8080/api/blocs'; // URL backend Spring Boot

  constructor(private http: HttpClient) { }

  // Récupérer les blocs d’un magasin par son ID
  getBlocsByMagasinId(magasinId: number): Observable<Bloc[]> {
    return this.http.get<Bloc[]>(`${this.baseUrl}/magasin/${magasinId}`);
  }
}
