import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';
import { JeuDeleteDialogComponent } from './jeu-delete-dialog.component';

@Component({
  selector: 'jhi-jeu',
  templateUrl: './jeu.component.html',
})
export class JeuComponent implements OnInit, OnDestroy {
  jeus?: IJeu[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected jeuService: JeuService,
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
      this.jeuService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IJeu[]>) => (this.jeus = res.body || []));
      return;
    }

    this.jeuService.query().subscribe((res: HttpResponse<IJeu[]>) => (this.jeus = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJeus();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJeu): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJeus(): void {
    this.eventSubscriber = this.eventManager.subscribe('jeuListModification', () => this.loadAll());
  }

  delete(jeu: IJeu): void {
    const modalRef = this.modalService.open(JeuDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jeu = jeu;
  }
}
