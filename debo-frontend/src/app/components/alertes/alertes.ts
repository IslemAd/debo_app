import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlerteService } from '../../services/alerte.service';
import { Alerte } from '../../models/alerte.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-alertes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alertes.html',
  styleUrl: './alertes.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AlertesComponent implements OnInit, OnDestroy {

  alertes: Alerte[] = [];
  message = '';
  erreur = '';
  private destroy$ = new Subject<void>();

  constructor(
    private alerteService: AlerteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAlertes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadAlertes(): void {
    this.alerteService.getNonResolues().pipe(takeUntil(this.destroy$)).subscribe({
      next: (data) => {
        this.alertes = [...data];
        this.cdr.markForCheck();
      },
      error: () => {
        this.erreur = '❌ Impossible de charger les alertes';
        this.cdr.markForCheck();
      }
    });
  }

  resoudre(id: number): void {
    this.alerteService.resoudre(id).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.message = '✅ Alerte résolue !';
        this.erreur = '';
        this.cdr.markForCheck();
        this.loadAlertes();
      },
      error: err => {
        this.erreur = '❌ Erreur : ' + (err.error?.erreur || err.error?.message || err.message);
        this.cdr.markForCheck();
      }
    });
  }

  trackByAlerte(index: number, alerte: Alerte): number {
    return alerte.id ?? index;
  }
}
