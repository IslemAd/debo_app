import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alerte } from '../models/alerte.model';

@Injectable({ providedIn: 'root' })
export class AlerteService {

  private apiUrl = 'http://localhost:8081/api/alertes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(this.apiUrl);
  }

  getNonResolues(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/non-resolues`);
  }

  resoudre(id: number): Observable<Alerte> {
    return this.http.patch<Alerte>(`${this.apiUrl}/${id}/resoudre`, {});
  }
}
