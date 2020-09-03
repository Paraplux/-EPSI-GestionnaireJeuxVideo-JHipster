import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlateforme } from 'app/shared/model/plateforme.model';
import { PlateformeService } from './plateforme.service';

@Component({
  templateUrl: './plateforme-delete-dialog.component.html',
})
export class PlateformeDeleteDialogComponent {
  plateforme?: IPlateforme;

  constructor(
    protected plateformeService: PlateformeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plateformeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('plateformeListModification');
      this.activeModal.close();
    });
  }
}
