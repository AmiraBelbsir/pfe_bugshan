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
  // Méthode pour créer une nouvelle pièce
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
    compatibilite: string,
    precommandable: boolean,
    magasinId: number,
    blocId: number
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
    formData.append('compatibilite', pieceData.compatibilite);
    formData.append('precommandable', pieceData.precommandable ? 'true' : 'false');


    // ✅ Ajouter magasinId et blocId
    formData.append('magasinId', pieceData.magasinId.toString());
    formData.append('blocId', pieceData.blocId.toString());

    return this.http.post<any>(this.apiUrl, formData);
  }
  updatePiece(
    pieceId: number,
    pieceData: {
      active: boolean;
      nom: string,
      reference: string,
      marque: string,
      prix: number,
      quantite: number,
      quantiteMinimum: number,
      description: string,
      compatibilite: string,
      type: string,
      dateAchat: string,
      precommandable: boolean,
      magasinId: number,
      blocId: number
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
    formData.append('compatibilite', pieceData.compatibilite);
    formData.append('type', pieceData.type); // ✅ string déjà correct pour enum
    formData.append('dateAchat', pieceData.dateAchat);
    formData.append('precommandable', pieceData.precommandable.toString());
    formData.append('active', pieceData.active.toString());  // Convert boolean to string for the form data

    formData.append('magasinId', pieceData.magasinId.toString());
    formData.append('blocId', pieceData.blocId.toString());

    if (imageFile) {
      formData.append('file', imageFile);
    }

    return this.http.put<any>(`${this.apiUrl}/${pieceId}`, formData);
  }

}

