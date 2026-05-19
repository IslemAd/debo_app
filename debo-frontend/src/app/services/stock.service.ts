import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stock } from '../models/stock.model';

@Injectable({ providedIn: 'root' })
export class StockService {

  private apiUrl = 'http://localhost:18081/api/stocks';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Stock[]> {
    return this.http.get<Stock[]>(this.apiUrl);
  }

  getStocksBas(): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.apiUrl}/alertes`);
  }

  getById(id: number): Observable<Stock> {
  return this.http.get<Stock>(`${this.apiUrl}/${id}`);
}

  create(stock: Stock): Observable<Stock> {
    return this.http.post<Stock>(this.apiUrl, {
      produitId: stock.produit.id,
      entrepotId: stock.entrepot.id,
      quantite: stock.quantite,
      seuilAlerte: stock.seuilAlerte
    });
  }

  update(id: number, stock: Stock): Observable<Stock> {
    return this.http.put<Stock>(`${this.apiUrl}/${id}`, {
      produitId: stock.produit.id,
      entrepotId: stock.entrepot.id,
      quantite: stock.quantite,
      seuilAlerte: stock.seuilAlerte
    });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
