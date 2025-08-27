import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ApplyPromoRequest, ApplyPromoResponse, CodePromo} from '../models/code-promo';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CodePromoService {

  private apiUrl = `http://localhost:8080/api/codes-promo`;

  constructor(private http: HttpClient) { }

  applyPromo(request: ApplyPromoRequest): Observable<ApplyPromoResponse> {
    return this.http.post<ApplyPromoResponse>(`${this.apiUrl}/apply`, request);
  }
}
