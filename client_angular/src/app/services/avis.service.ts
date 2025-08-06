import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bloc } from '../models/bloc';
import {Avis} from "../models/avis";


@Injectable({
  providedIn: 'root'
})
export class AvisService {
  private apiUrl = 'http://localhost:8080/api/avis';

  constructor(private http: HttpClient) {
  }

  // Obtenir tous les blocs
  getAllAvis(): Observable<Avis[]> {
    return this.http.get<Avis[]>(this.apiUrl);
  }
}
