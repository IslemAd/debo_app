import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EntrepotService } from '../../services/entrepot.service';
import { Entrepot } from '../../models/entrepot.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-entrepots',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './entrepots.html',
  styleUrl: './entrepots.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EntrepotsComponent implements OnInit, OnDestroy {

  entrepots: Entrepot[] = [];
  entrepot: Entrepot = { nom: '', adresse: '', capacite: 0 };
  isEditing = false;
  selectedId?: number;
  message = '';
  erreur = '';
  private destroy$ = new Subject<void>();

  constructor(
    private entrepotService: EntrepotService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadEntrepots();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadEntrepots(): void {
    this.entrepotService.getAll().pipe(takeUntil(this.destroy$)).subscribe({
      next: (data) => {
        this.entrepots = [...data];
        this.cdr.markForCheck();
      },
      error: () => {
        this.erreur = '❌ Impossible de charger les entrepôts';
        this.cdr.markForCheck();
      }
    });
  }

  save(): void {
    this.message = '';
    this.erreur = '';
    const entrepotData: Entrepot = { ...this.entrepot };

    if (this.isEditing && this.selectedId) {
      this.entrepotService.update(this.selectedId, entrepotData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Entrepôt modifié avec succès !';
          this.cdr.markForCheck();
          this.reset();
          this.loadEntrepots();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    } else {
      this.entrepotService.create(entrepotData).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Entrepôt créé avec succès !';
          this.cdr.markForCheck();
          this.reset();
          this.loadEntrepots();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  edit(e: Entrepot): void {
    this.entrepot = { ...e };
    this.isEditing = true;
    this.selectedId = e.id;
    this.message = '';
    this.erreur = '';
  }

  delete(id: number): void {
    if (confirm('Confirmer la suppression ?')) {
      this.entrepotService.delete(id).pipe(takeUntil(this.destroy$)).subscribe({
        next: () => {
          this.message = '✅ Entrepôt supprimé !';
          this.loadEntrepots();
        },
        error: err => {
          this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
          this.cdr.markForCheck();
        }
      });
    }
  }

  reset(): void {
    this.entrepot = { nom: '', adresse: '', capacite: 0 };
    this.isEditing = false;
    this.selectedId = undefined;
  }

  trackByEntrepot(index: number, entrepot: Entrepot): number {
    return entrepot.id ?? index;
  }
}
