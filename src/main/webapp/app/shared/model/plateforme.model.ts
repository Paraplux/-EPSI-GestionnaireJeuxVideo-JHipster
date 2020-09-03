export interface IPlateforme {
  id?: number;
  name?: string;
}

export class Plateforme implements IPlateforme {
  constructor(public id?: number, public name?: string) {}
}
