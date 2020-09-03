import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJeu, Jeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';
import { IPlateforme } from 'app/shared/model/plateforme.model';
import { PlateformeService } from 'app/entities/plateforme/plateforme.service';

@Component({
  selector: 'jhi-jeu-update',
  templateUrl: './jeu-update.component.html',
})
export class JeuUpdateComponent implements OnInit {
  isSaving = false;
  plateformes: IPlateforme[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    prix: [],
    plateforme: [],
  });

  constructor(
    protected jeuService: JeuService,
    protected plateformeService: PlateformeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jeu }) => {
      this.updateForm(jeu);

      this.plateformeService.query().subscribe((res: HttpResponse<IPlateforme[]>) => (this.plateformes = res.body || []));
    });
  }

  updateForm(jeu: IJeu): void {
    this.editForm.patchValue({
      id: jeu.id,
      name: jeu.name,
      prix: jeu.prix,
      plateforme: jeu.plateforme,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jeu = this.createFromForm();
    if (jeu.id !== undefined) {
      this.subscribeToSaveResponse(this.jeuService.update(jeu));
    } else {
      this.subscribeToSaveResponse(this.jeuService.create(jeu));
    }
  }

  private createFromForm(): IJeu {
    return {
      ...new Jeu(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      plateforme: this.editForm.get(['plateforme'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJeu>>): void {
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

  trackById(index: number, item: IPlateforme): any {
    return item.id;
  }
}
