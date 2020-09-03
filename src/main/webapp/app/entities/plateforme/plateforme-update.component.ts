import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlateforme, Plateforme } from 'app/shared/model/plateforme.model';
import { PlateformeService } from './plateforme.service';

@Component({
  selector: 'jhi-plateforme-update',
  templateUrl: './plateforme-update.component.html',
})
export class PlateformeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected plateformeService: PlateformeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateforme }) => {
      this.updateForm(plateforme);
    });
  }

  updateForm(plateforme: IPlateforme): void {
    this.editForm.patchValue({
      id: plateforme.id,
      name: plateforme.name,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plateforme = this.createFromForm();
    if (plateforme.id !== undefined) {
      this.subscribeToSaveResponse(this.plateformeService.update(plateforme));
    } else {
      this.subscribeToSaveResponse(this.plateformeService.create(plateforme));
    }
  }

  private createFromForm(): IPlateforme {
    return {
      ...new Plateforme(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlateforme>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
