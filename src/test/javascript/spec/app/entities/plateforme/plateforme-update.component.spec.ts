import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JeuxvideosTestModule } from '../../../test.module';
import { PlateformeUpdateComponent } from 'app/entities/plateforme/plateforme-update.component';
import { PlateformeService } from 'app/entities/plateforme/plateforme.service';
import { Plateforme } from 'app/shared/model/plateforme.model';

describe('Component Tests', () => {
  describe('Plateforme Management Update Component', () => {
    let comp: PlateformeUpdateComponent;
    let fixture: ComponentFixture<PlateformeUpdateComponent>;
    let service: PlateformeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JeuxvideosTestModule],
        declarations: [PlateformeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PlateformeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlateformeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlateformeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plateforme(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Plateforme();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
