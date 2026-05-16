import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MouvementStockService } from '../../services/mouvement-stock.service';
import { ProduitService } from '../../services/produit.service';
import { EntrepotService } from '../../services/entrepot.service';
import { MouvementStock } from '../../models/mouvement-stock.model';
import { Produit } from '../../models/produit.model';
import { Entrepot } from '../../models/entrepot.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-mouvements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mouvements.html',
  styleUrl: './mouvements.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MouvementsComponent implements OnInit, OnDestroy {

  mouvements: MouvementStock[] = [];
  produits: Produit[] = [];
  entrepots: Entrepot[] = [];

  mouvement: MouvementStock = {
    type: 'ENTREE',
    quantite: 1,
    produit: null as any,
    entrepot: null as any
  };

  message = '';
  erreur = '';
  private destroy$ = new Subject<void>();

  constructor(
    private mouvementService: MouvementStockService,
    private produitService: ProduitService,
    private entrepotService: EntrepotService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMouvements();

    this.produitService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: data => {
        this.produits = [...data];
        if (data.length > 0 && !this.mouvement.produit) this.mouvement.produit = data[0];
        this.cdr.markForCheck();
      },
      error: () => { this.erreur = '❌ Impossible de charger les produits'; this.cdr.markForCheck(); }
    });

    this.entrepotService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: data => {
        this.entrepots = [...data];
        if (data.length > 0 && !this.mouvement.entrepot) this.mouvement.entrepot = data[0];
        this.cdr.markForCheck();
      },
      error: () => { this.erreur = '❌ Impossible de charger les entrepôts'; this.cdr.markForCheck(); }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadMouvements(): void {
    this.mouvementService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: (data) => { this.mouvements = [...data]; this.cdr.markForCheck(); },
      error: () => { this.erreur = '❌ Impossible de charger les mouvements'; this.cdr.markForCheck(); }
    });
  }

  save(): void {
    this.erreur = '';
    this.message = '';

    if (!this.mouvement.produit?.id || !this.mouvement.entrepot?.id) {
      this.erreur = '❌ Veuillez sélectionner un produit et un entrepôt';
      this.cdr.markForCheck();
      return;
    }

    if (this.mouvement.quantite < 1) {
      this.erreur = '❌ La quantité doit être au moins 1';
      this.cdr.markForCheck();
      return;
    }

    this.mouvementService.create(this.mouvement).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.message = '✅ Mouvement enregistré avec succès !';
        this.cdr.markForCheck();
        this.resetForm();
        this.loadMouvements();
      },
      error: err => {
        this.erreur = '❌ ' + (err.error?.erreur || err.error?.message || 'Stock insuffisant ou données invalides');
        this.cdr.markForCheck();
      }
    });
  }

  resetForm(): void {
    this.mouvement = {
      type: 'ENTREE',
      quantite: 1,
      produit: this.produits[0] ?? null as any,
      entrepot: this.entrepots[0] ?? null as any
    };
  }

  trackByMouvement(index: number, mouvement: MouvementStock): number {
    return mouvement.id ?? index;
  }
}
