import { Produit } from './produit.model';
import { Entrepot } from './entrepot.model';

export interface MouvementStock {
  id?: number;
  type: 'ENTREE' | 'SORTIE';
  quantite: number;
  date?: string;
  produit: Produit;
  entrepot: Entrepot;
}
