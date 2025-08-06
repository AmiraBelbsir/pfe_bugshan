import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicule } from '../models/vehicule';

@Injectable({
  providedIn: 'root'
})
export class VehiculeService {
  private apiUrl = 'http://localhost:8080/api/vehicules';

  constructor(private http: HttpClient) {
  }

  getAllVehicles(): Observable<Vehicule[]> {
    return this.http.get<Vehicule[]>(this.apiUrl);
  }

  getVehicleById(id: number): Observable<Vehicule> {
    return this.http.get<Vehicule>(`${this.apiUrl}/${id}`);
  }

  createVehicule(vehiculeData: {
    marque: string,
    modele: string,
    quantite: number,
    annee: number,
    couleur: string,
    sieges: number,
    prix: number,
    disponible: boolean,
    emplacement: string,
    niveauCarburant: number,
    typeVehicule: string,
    typeCarburant: string,
    typeTransmission: string
  }, file?: File, photosAdditionnelles?: File[]): Observable<any> {
    const formData = new FormData();

    if (file) {
      formData.append('file', file);
    }

    if (photosAdditionnelles && photosAdditionnelles.length > 0) {
      photosAdditionnelles.forEach(photo => {
        formData.append('photosAdditionnelles', photo);
      });
    }

    formData.append('marque', vehiculeData.marque);
    formData.append('modele', vehiculeData.modele);
    formData.append('quantite', vehiculeData.quantite.toString());
    formData.append('annee', vehiculeData.annee.toString());
    formData.append('couleur', vehiculeData.couleur);
    formData.append('sieges', vehiculeData.sieges.toString());
    formData.append('prix', vehiculeData.prix.toString());
    formData.append('disponible', vehiculeData.disponible.toString());
    formData.append('emplacement', vehiculeData.emplacement);
    formData.append('niveauCarburant', vehiculeData.niveauCarburant.toString());
    formData.append('typeVehicule', vehiculeData.typeVehicule.toUpperCase());
    formData.append('typeCarburant', vehiculeData.typeCarburant.toUpperCase());
    formData.append('typeTransmission', vehiculeData.typeTransmission.toUpperCase());

    return this.http.post<any>(this.apiUrl, formData);
  }


  updateVehicule(vehiculeId: number, vehiculeData: {
    marque: string,
    modele: string,
    quantite: number,
    annee: number,
    couleur: string,
    sieges: number,
    prix: number,
    disponible: boolean,
    emplacement: string,
    niveauCarburant: number,
    typeVehicule: string,
    typeCarburant: string,
    typeTransmission: string
  }, file?: File, photosAdditionnelles?: File[]): Observable<any> {
    const formData = new FormData();

    if (file) {
      formData.append('file', file);
    }

    if (photosAdditionnelles && photosAdditionnelles.length > 0) {
      photosAdditionnelles.forEach(photo => {
        formData.append('photosAdditionnelles', photo);
      });
    }

    formData.append('marque', vehiculeData.marque);
    formData.append('modele', vehiculeData.modele);
    formData.append('quantite', vehiculeData.quantite.toString());
    formData.append('annee', vehiculeData.annee.toString());
    formData.append('couleur', vehiculeData.couleur);
    formData.append('sieges', vehiculeData.sieges.toString());
    formData.append('prix', vehiculeData.prix.toString());
    formData.append('disponible', vehiculeData.disponible.toString());
    formData.append('emplacement', vehiculeData.emplacement);
    formData.append('niveauCarburant', vehiculeData.niveauCarburant.toString());
    formData.append('typeVehicule', vehiculeData.typeVehicule.toUpperCase());
    formData.append('typeCarburant', vehiculeData.typeCarburant.toUpperCase());
    formData.append('typeTransmission', vehiculeData.typeTransmission.toUpperCase());

    return this.http.put<any>(`${this.apiUrl}/${vehiculeId}`, formData);
  }

}
