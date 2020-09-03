import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JeuxvideosSharedModule } from 'app/shared/shared.module';
import { JeuComponent } from './jeu.component';
import { JeuDetailComponent } from './jeu-detail.component';
import { JeuUpdateComponent } from './jeu-update.component';
import { JeuDeleteDialogComponent } from './jeu-delete-dialog.component';
import { jeuRoute } from './jeu.route';

@NgModule({
  imports: [JeuxvideosSharedModule, RouterModule.forChild(jeuRoute)],
  declarations: [JeuComponent, JeuDetailComponent, JeuUpdateComponent, JeuDeleteDialogComponent],
  entryComponents: [JeuDeleteDialogComponent],
})
export class JeuxvideosJeuModule {}
