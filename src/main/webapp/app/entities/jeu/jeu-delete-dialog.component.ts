import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';

@Component({
  templateUrl: './jeu-delete-dialog.component.html',
})
export class JeuDeleteDialogComponent {
  jeu?: IJeu;

  constructor(protected jeuService: JeuService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jeuService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jeuListModification');
      this.activeModal.close();
    });
  }
}
