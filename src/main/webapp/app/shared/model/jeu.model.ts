import { IPlateforme } from 'app/shared/model/plateforme.model';

export interface IJeu {
  id?: number;
  name?: string;
  prix?: number;
  plateforme?: IPlateforme;
}

export class Jeu implements IJeu {
  constructor(public id?: number, public name?: string, public prix?: number, public plateforme?: IPlateforme) {}
}
