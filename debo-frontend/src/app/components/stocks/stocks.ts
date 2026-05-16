import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StockService } from '../../services/stock.service';
import { ProduitService } from '../../services/produit.service';
import { EntrepotService } from '../../services/entrepot.service';
import { Stock } from '../../models/stock.model';
import { Produit } from '../../models/produit.model';
import { Entrepot } from '../../models/entrepot.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-stocks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stocks.html',
  styleUrl: './stocks.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StocksComponent implements OnInit, OnDestroy {

  stocks: Stock[] = [];
  produits: Produit[] = [];
  entrepots: Entrepot[] = [];

  stock: Stock = {
    quantite: 0,
    seuilAlerte: 0,
    produit: null as any,
    entrepot: null as any
  };

  isEditing = false;
  selectedId?: number;
  message = '';
  erreur = '';
  private destroy$ = new Subject<void>();

  constructor(
    private stockService: StockService,
    private produitService: ProduitService,
    private entrepotService: EntrepotService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStocks();

    this.produitService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: data => {
        this.produits = [...data];
        if (data.length > 0 && !this.stock.produit) this.stock.produit = data[0];
        this.cdr.markForCheck();
      },
      error: () => { this.erreur = '❌ Impossible de charger les produits'; this.cdr.markForCheck(); }
    });

    this.entrepotService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: data => {
        this.entrepots = [...data];
        if (data.length > 0 && !this.stock.entrepot) this.stock.entrepot = data[0];
        this.cdr.markForCheck();
      },
      error: () => { this.erreur = '❌ Impossible de charger les entrepôts'; this.cdr.markForCheck(); }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadStocks(): void {
    this.stockService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: (data) => { this.stocks = [...data]; this.cdr.markForCheck(); },
      error: () => { this.erreur = '❌ Impossible de charger les stocks'; this.cdr.markForCheck(); }
    });
  }

  save(): void {
    this.message = '';
    this.erreur = '';

    if (!this.stock.produit?.id || !this.stock.entrepot?.id) {
      this.erreur = '❌ Veuillez sélectionner un produit et un entrepôt';
      this.cdr.markForCheck();
      return;
    }

    const stockData: Stock = { ...this.stock };

    if (this.isEditing && this.selectedId) {
      this.stockService.update(this.selectedId, stockData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Stock modifié avec succès !';
          this.cdr.markForCheck();
          this.reset();
          this.loadStocks();
        },
        error: err => {
          this.erreur = '❌ ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    } else {
      this.stockService.create(stockData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Stock créé avec succès !';
          this.cdr.markForCheck();
          this.reset();
          this.loadStocks();
        },
        error: err => {
          this.erreur = '❌ ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  edit(s: Stock): void {
    this.stock = { ...s };
    this.isEditing = true;
    this.selectedId = s.id;
    this.message = '';
    this.erreur = '';
  }

  delete(id: number): void {
    if (confirm('Confirmer la suppression ?')) {
      this.stockService.delete(id).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => { this.message = '✅ Stock supprimé !'; this.loadStocks(); },
        error: err => {
          this.erreur = '❌ ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  reset(): void {
    this.stock = {
      quantite: 0,
      seuilAlerte: 0,
      produit: this.produits[0] ?? null as any,
      entrepot: this.entrepots[0] ?? null as any
    };
    this.isEditing = false;
    this.selectedId = undefined;
  }

  trackByStock(index: number, stock: Stock): number {
    return stock.id ?? index;
  }
}
