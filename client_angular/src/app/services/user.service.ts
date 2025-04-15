import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }
  createUserWithImage(userData: {
    fullName: string,
    email: string,
    username: string,
    password: string,
    phoneNumber: string,
    gender: string,
    role: string,
    city: string,
    address: string
  }, file?: File): Observable<any> {
    const formData = new FormData();

    // Append file if provided
    if (file) {
      formData.append('file', file);
    }

    // Append required fields
    formData.append('fullName', userData.fullName);
    formData.append('email', userData.email);
    formData.append('username', userData.username);
    formData.append('password', userData.password);
    formData.append('phoneNumber', userData.phoneNumber);
    formData.append('gender', userData.gender.toUpperCase()); // Match backend enum format
    formData.append('role', userData.role.toUpperCase()); // Match backend enum format
    formData.append('city', userData.city.toUpperCase()); // Match backend enum format
    formData.append('address', userData.address);

    // Send the form data in a POST request
    return this.http.post<any>(this.apiUrl, formData);
  }

  updateUser(userId: number, userData: { fullName: string, email: string, username: string, phoneNumber: string, gender: string, role: string, city: string, address: string, active: boolean }, imageFile?: File): Observable<any> {
    const formData = new FormData();
    formData.append('fullName', userData.fullName);
    formData.append('email', userData.email);
    formData.append('username', userData.username);
    formData.append('phoneNumber', userData.phoneNumber);
    formData.append('gender', userData.gender.toUpperCase()); // Match backend enum format
    formData.append('role', userData.role.toUpperCase()); // Match backend enum format
    formData.append('city', userData.city.toUpperCase()); // Match backend enum format
    formData.append('address', userData.address);
    formData.append('active', userData.active.toString());  // Convert boolean to string for the form data

    if (imageFile) {
      formData.append('file', imageFile); // Only append image if it's provided
    }

    return this.http.put<any>(`${this.apiUrl}/${userId}`, formData);
  }


}
