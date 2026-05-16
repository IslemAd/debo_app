import { Produit } from './produit.model';
import { Entrepot } from './entrepot.model';

export interface Stock {
  id?: number;
  quantite: number;
  seuilAlerte: number;
  produit: Produit;
  entrepot: Entrepot;
}
