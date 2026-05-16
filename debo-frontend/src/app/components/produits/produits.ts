import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProduitService } from '../../services/produit.service';
import { Produit } from '../../models/produit.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-produits',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './produits.html',
  styleUrl: './produits.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProduitsComponent implements OnInit, OnDestroy {

  produits: Produit[] = [];
  produit: Produit = { nom: '', categorie: '', prix: 0, fournisseur: '', seuilMin: 0 };
  isEditing = false;
  selectedId?: number;
  message = '';
  erreur = '';
  private destroy$ = new Subject<void>();

  constructor(
    private produitService: ProduitService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProduits();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadProduits(): void {
    this.produitService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: (data) => {
        this.produits = [...data];
        this.cdr.markForCheck();
      },
      error: () => {
        this.erreur = '❌ Impossible de charger les produits';
        this.cdr.markForCheck();
      }
    });
  }

  save(): void {
    this.message = '';
    this.erreur = '';
    const produitData: Produit = { ...this.produit };

    if (this.isEditing && this.selectedId) {
      this.produitService.update(this.selectedId, produitData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Produit modifié !';
          this.cdr.markForCheck();
          this.reset();
          this.loadProduits();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    } else {
      this.produitService.create(produitData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Produit créé !';
          this.cdr.markForCheck();
          this.reset();
          this.loadProduits();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  edit(p: Produit): void {
    this.produit = { ...p };
    this.isEditing = true;
    this.selectedId = p.id;
    this.message = '';
    this.erreur = '';
  }

  delete(id: number): void {
    if (confirm('Confirmer la suppression ?')) {
      this.produitService.delete(id).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Produit supprimé !';
          this.loadProduits();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  reset(): void {
    this.produit = { nom: '', categorie: '', prix: 0, fournisseur: '', seuilMin: 0 };
    this.isEditing = false;
    this.selectedId = undefined;
  }

  trackByProduit(index: number, produit: Produit): number {
    return produit.id ?? index;
  }
}
