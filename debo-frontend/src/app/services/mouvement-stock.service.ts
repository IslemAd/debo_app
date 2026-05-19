import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MouvementStock } from '../models/mouvement-stock.model';

@Injectable({ providedIn: 'root' })
export class MouvementStockService {

  private apiUrl = 'http://localhost:18081/api/mouvements';

  constructor(private http: HttpClient) {}

  getAll(): Observable<MouvementStock[]> {
    return this.http.get<MouvementStock[]>(this.apiUrl);
  }

  create(mouvement: MouvementStock): Observable<MouvementStock> {
    return this.http.post<MouvementStock>(this.apiUrl, {
      produitId: mouvement.produit.id,
      entrepotId: mouvement.entrepot.id,
      type: mouvement.type,
      quantite: mouvement.quantite
    });
  }
}
