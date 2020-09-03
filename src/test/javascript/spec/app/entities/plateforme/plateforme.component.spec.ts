import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JeuxvideosTestModule } from '../../../test.module';
import { PlateformeComponent } from 'app/entities/plateforme/plateforme.component';
import { PlateformeService } from 'app/entities/plateforme/plateforme.service';
import { Plateforme } from 'app/shared/model/plateforme.model';

describe('Component Tests', () => {
  describe('Plateforme Management Component', () => {
    let comp: PlateformeComponent;
    let fixture: ComponentFixture<PlateformeComponent>;
    let service: PlateformeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JeuxvideosTestModule],
        declarations: [PlateformeComponent],
      })
        .overrideTemplate(PlateformeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlateformeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlateformeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Plateforme(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.plateformes && comp.plateformes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
