import { Stock } from './stock.model';

export interface Alerte {
  id?: number;
  message: string;
  resolue: boolean;
  dateCreation?: string;
  stock: Stock;
}
