import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JeuxvideosSharedModule } from 'app/shared/shared.module';
import { PlateformeComponent } from './plateforme.component';
import { PlateformeDetailComponent } from './plateforme-detail.component';
import { PlateformeUpdateComponent } from './plateforme-update.component';
import { PlateformeDeleteDialogComponent } from './plateforme-delete-dialog.component';
import { plateformeRoute } from './plateforme.route';

@NgModule({
  imports: [JeuxvideosSharedModule, RouterModule.forChild(plateformeRoute)],
  declarations: [PlateformeComponent, PlateformeDetailComponent, PlateformeUpdateComponent, PlateformeDeleteDialogComponent],
  entryComponents: [PlateformeDeleteDialogComponent],
})
export class JeuxvideosPlateformeModule {}
