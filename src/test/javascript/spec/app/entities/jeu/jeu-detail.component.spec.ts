import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JeuxvideosTestModule } from '../../../test.module';
import { JeuDetailComponent } from 'app/entities/jeu/jeu-detail.component';
import { Jeu } from 'app/shared/model/jeu.model';

describe('Component Tests', () => {
  describe('Jeu Management Detail Component', () => {
    let comp: JeuDetailComponent;
    let fixture: ComponentFixture<JeuDetailComponent>;
    const route = ({ data: of({ jeu: new Jeu(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JeuxvideosTestModule],
        declarations: [JeuDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(JeuDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JeuDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jeu on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jeu).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
