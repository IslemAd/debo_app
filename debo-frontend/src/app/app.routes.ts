import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard';
import { EntrepotsComponent } from './components/entrepots/entrepots';
import { ProduitsComponent } from './components/produits/produits';
import { StocksComponent } from './components/stocks/stocks';
import { MouvementsComponent } from './components/mouvements/mouvements';
import { AlertesComponent } from './components/alertes/alertes';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard',  component: DashboardComponent },
  { path: 'entrepots',  component: EntrepotsComponent },
  { path: 'produits',   component: ProduitsComponent },
  { path: 'stocks',     component: StocksComponent },
  { path: 'mouvements', component: MouvementsComponent },
  { path: 'alertes',    component: AlertesComponent }
];
