import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'jeu',
        loadChildren: () => import('./jeu/jeu.module').then(m => m.JeuxvideosJeuModule),
      },
      {
        path: 'plateforme',
        loadChildren: () => import('./plateforme/plateforme.module').then(m => m.JeuxvideosPlateformeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JeuxvideosEntityModule {}
