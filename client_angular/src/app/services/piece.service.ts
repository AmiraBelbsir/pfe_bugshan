import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Piece } from '../models/piece'; // chemin vers ton modèle Piece




@Injectable({
  providedIn: 'root'
})
export class PieceService {

  private apiUrl = 'http://localhost:8080/api/piece'; // URL de ton API backend

  constructor(private http: HttpClient) { }

  // Méthode pour récupérer la liste des pièces depuis le backend
  getPieces(): Observable<Piece[]> {
    return this.http.get<Piece[]>(this.apiUrl);
  }
  getPieceById(id: number): Observable<Piece> {
    return this.http.get<Piece>(`${this.apiUrl}/${id}`);
  }
  createPieceWithImage(pieceData: {
    nom: string,
    reference: string,
    marque: string,
    prix: number,
    quantite: number,
    imageUrl?: string,
    type: string,
    dateAchat: string,
    quantiteMinimum: number,
    description: string,
    precommandable: boolean,
    magasinId: number,
    blocId: number,
    fournisseurId?: number,
    vehiculeIds?: number[]
  }, file?: File): Observable<any> {
    const formData = new FormData();

    if (file) {
      formData.append('file', file);
    }

    formData.append('nom', pieceData.nom);
    formData.append('reference', pieceData.reference);
    formData.append('marque', pieceData.marque);
    formData.append('prix', pieceData.prix.toString());
    formData.append('quantite', pieceData.quantite.toString());
    formData.append('type', pieceData.type);
    formData.append('dateAchat', pieceData.dateAchat);
    formData.append('quantiteMinimum', pieceData.quantiteMinimum.toString());
    formData.append('description', pieceData.description);
    formData.append('precommandable', pieceData.precommandable ? 'true' : 'false');

    formData.append('magasinId', pieceData.magasinId.toString());
    formData.append('blocId', pieceData.blocId.toString());

    // Ajout fournisseurId si présent
    if (pieceData.fournisseurId != null) {
      formData.append('fournisseurId', pieceData.fournisseurId.toString());
    }

    // Ajout des vehiculeIds si présents
    if (pieceData.vehiculeIds && pieceData.vehiculeIds.length > 0) {
      pieceData.vehiculeIds.forEach(id => {
        formData.append('vehiculeIds', id.toString());
      });
    }

    return this.http.post<any>(this.apiUrl, formData);
  }


  updatePiece(
    pieceId: number,
    pieceData: {
      active: boolean;
      nom: string;
      reference: string;
      marque: string;
      prix: number;
      quantite: number;
      quantiteMinimum: number;
      description: string;
      type: string;
      dateAchat: string;
      precommandable: boolean;
      magasinId: number;
      blocId: number;
      fournisseurId?: number;          // optionnel selon ta logique
      vehiculeIds?: number[];          // optionnel selon ta logique
    },
    imageFile?: File
  ): Observable<any> {
    const formData = new FormData();

    formData.append('nom', pieceData.nom);
    formData.append('reference', pieceData.reference);
    formData.append('marque', pieceData.marque);
    formData.append('prix', pieceData.prix.toString());
    formData.append('quantite', pieceData.quantite.toString());
    formData.append('quantiteMinimum', pieceData.quantiteMinimum.toString());
    formData.append('description', pieceData.description);
    formData.append('type', pieceData.type);
    formData.append('dateAchat', pieceData.dateAchat);
    formData.append('precommandable', pieceData.precommandable.toString());
    formData.append('active', pieceData.active.toString());

    formData.append('magasinId', pieceData.magasinId.toString());
    formData.append('blocId', pieceData.blocId.toString());

    if (pieceData.fournisseurId != null) {
      formData.append('fournisseurId', pieceData.fournisseurId.toString());
    }

    if (pieceData.vehiculeIds && pieceData.vehiculeIds.length > 0) {
      pieceData.vehiculeIds.forEach(id => {
        formData.append('vehiculeIds', id.toString());
      });
    }

    if (imageFile) {
      formData.append('file', imageFile);
    }

    return this.http.put<any>(`${this.apiUrl}/${pieceId}`, formData);
  }

}

