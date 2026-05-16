import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Entrepot } from '../models/entrepot.model';

@Injectable({ providedIn: 'root' })
export class EntrepotService {

  private apiUrl = 'http://localhost:8081/api/entrepots';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Entrepot[]> {
    return this.http.get<Entrepot[]>(this.apiUrl);
  }

  getById(id: number): Observable<Entrepot> {
    return this.http.get<Entrepot>(`${this.apiUrl}/${id}`);
  }

  create(entrepot: Entrepot): Observable<Entrepot> {
    return this.http.post<Entrepot>(this.apiUrl, entrepot);
  }

  update(id: number, entrepot: Entrepot): Observable<Entrepot> {
    return this.http.put<Entrepot>(`${this.apiUrl}/${id}`, entrepot);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
