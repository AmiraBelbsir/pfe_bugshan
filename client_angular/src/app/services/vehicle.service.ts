import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from '../models/vehicle';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private apiUrl = 'http://localhost:8080/api/vehicles';

  constructor(private http: HttpClient) {
  }

  getAllVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiUrl);
  }

  getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  createVehicle(vehicleData: {
    make: string,
    model: string,
    quantity: number,
    year: number,
    color: string,
    vin: string,
    mileage: number,
    seats: number,
    retailPrice: number,
    available: boolean,
    location: string,
    insured: boolean,
    vehicleCondition: string,
    fuelLevel: number,
    vehicleType: string,
    fuelType: string,
    transmissionType: string
  }, file?: File, additionalFiles?: File[]): Observable<any> {
    const formData = new FormData();

    // Append the main image file if provided
    if (file) {
      formData.append('file', file);
    }

    // Append additional photos if provided
    if (additionalFiles && additionalFiles.length > 0) {
      additionalFiles.forEach(additionalFile => {
        formData.append('additionalFiles', additionalFile);
      });
    }

    // Append the vehicle data
    formData.append('make', vehicleData.make);
    formData.append('model', vehicleData.model);
    formData.append('quantity', vehicleData.quantity.toString());
    formData.append('year', vehicleData.year.toString());
    formData.append('color', vehicleData.color);
    formData.append('vin', vehicleData.vin);
    formData.append('mileage', vehicleData.mileage.toString());
    formData.append('seats', vehicleData.seats.toString());
    formData.append('retailPrice', vehicleData.retailPrice.toString());
    formData.append('available', vehicleData.available.toString());
    formData.append('location', vehicleData.location);
    formData.append('insured', vehicleData.insured.toString());
    formData.append('condition', vehicleData.vehicleCondition.toUpperCase());
    formData.append('fuelLevel', vehicleData.fuelLevel.toString());
    formData.append('vehicleType', vehicleData.vehicleType.toUpperCase());
    formData.append('fuelType', vehicleData.fuelType.toUpperCase());
    formData.append('transmissionType', vehicleData.transmissionType.toUpperCase());

    return this.http.post<any>(this.apiUrl, formData);
  }

  updateVehicle(vehicleId: number, vehicleData: {
    make: string,
    model: string,
    quantity: number,
    year: number,
    color: string,
    vin: string,
    mileage: number,
    seats: number,
    retailPrice: number,
    available: boolean,
    location: string,
    insured: boolean,
    vehicleCondition: string,
    fuelLevel: number,
    vehicleType: string,
    fuelType: string,
    transmissionType: string
  }, file?: File, additionalFiles?: File[]): Observable<any> {
    const formData = new FormData();

    // Append the main image file if provided
    if (file) {
      formData.append('file', file);
    }

    // Append additional photos if provided
    if (additionalFiles && additionalFiles.length > 0) {
      additionalFiles.forEach(additionalFile => {
        formData.append('additionalFiles', additionalFile);
      });
    }

    // Append the vehicle data
    formData.append('make', vehicleData.make);
    formData.append('model', vehicleData.model);
    formData.append('quantity', vehicleData.quantity.toString());
    formData.append('year', vehicleData.year.toString());
    formData.append('color', vehicleData.color);
    formData.append('vin', vehicleData.vin);
    formData.append('mileage', vehicleData.mileage.toString());
    formData.append('seats', vehicleData.seats.toString());
    formData.append('retailPrice', vehicleData.retailPrice.toString());
    formData.append('available', vehicleData.available.toString());
    formData.append('location', vehicleData.location);
    formData.append('insured', vehicleData.insured.toString());
    formData.append('condition', vehicleData.vehicleCondition.toUpperCase());
    formData.append('fuelLevel', vehicleData.fuelLevel.toString());
    formData.append('vehicleType', vehicleData.vehicleType.toUpperCase());
    formData.append('fuelType', vehicleData.fuelType.toUpperCase());
    formData.append('transmissionType', vehicleData.transmissionType.toUpperCase());

    return this.http.put<any>(`${this.apiUrl}/${vehicleId}`, formData);
  }
}
