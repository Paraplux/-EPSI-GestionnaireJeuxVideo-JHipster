import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JeuxvideosTestModule } from '../../../test.module';
import { PlateformeDetailComponent } from 'app/entities/plateforme/plateforme-detail.component';
import { Plateforme } from 'app/shared/model/plateforme.model';

describe('Component Tests', () => {
  describe('Plateforme Management Detail Component', () => {
    let comp: PlateformeDetailComponent;
    let fixture: ComponentFixture<PlateformeDetailComponent>;
    const route = ({ data: of({ plateforme: new Plateforme(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JeuxvideosTestModule],
        declarations: [PlateformeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PlateformeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlateformeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load plateforme on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plateforme).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
