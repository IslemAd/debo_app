import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProduitService } from '../../services/produit.service';
import { EntrepotService } from '../../services/entrepot.service';
import { StockService } from '../../services/stock.service';
import { AlerteService } from '../../services/alerte.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent implements OnInit, OnDestroy {

  totalProduits = 0;
  totalEntrepots = 0;
  totalStocksBas = 0;
  totalAlertes = 0;
  private destroy$ = new Subject<void>();

  constructor(
    private produitService: ProduitService,
    private entrepotService: EntrepotService,
    private stockService: StockService,
    private alerteService: AlerteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.produitService.getAll().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.totalProduits = data.length;
      this.cdr.markForCheck();
    });

    this.entrepotService.getAll().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.totalEntrepots = data.length;
      this.cdr.markForCheck();
    });

    this.stockService.getStocksBas().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.totalStocksBas = data.length;
      this.cdr.markForCheck();
    });

    this.alerteService.getNonResolues().pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.totalAlertes = data.length;
      this.cdr.markForCheck();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
