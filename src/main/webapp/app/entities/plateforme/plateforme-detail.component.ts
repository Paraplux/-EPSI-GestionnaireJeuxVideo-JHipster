import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlateforme } from 'app/shared/model/plateforme.model';

@Component({
  selector: 'jhi-plateforme-detail',
  templateUrl: './plateforme-detail.component.html',
})
export class PlateformeDetailComponent implements OnInit {
  plateforme: IPlateforme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateforme }) => (this.plateforme = plateforme));
  }

  previousState(): void {
    window.history.back();
  }
}
