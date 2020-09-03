import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlateforme } from 'app/shared/model/plateforme.model';
import { PlateformeService } from './plateforme.service';
import { PlateformeDeleteDialogComponent } from './plateforme-delete-dialog.component';

@Component({
  selector: 'jhi-plateforme',
  templateUrl: './plateforme.component.html',
})
export class PlateformeComponent implements OnInit, OnDestroy {
  plateformes?: IPlateforme[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected plateformeService: PlateformeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.plateformeService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPlateforme[]>) => (this.plateformes = res.body || []));
      return;
    }

    this.plateformeService.query().subscribe((res: HttpResponse<IPlateforme[]>) => (this.plateformes = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPlateformes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlateforme): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPlateformes(): void {
    this.eventSubscriber = this.eventManager.subscribe('plateformeListModification', () => this.loadAll());
  }

  delete(plateforme: IPlateforme): void {
    const modalRef = this.modalService.open(PlateformeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plateforme = plateforme;
  }
}
