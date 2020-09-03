import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JeuxvideosTestModule } from '../../../test.module';
import { JeuComponent } from 'app/entities/jeu/jeu.component';
import { JeuService } from 'app/entities/jeu/jeu.service';
import { Jeu } from 'app/shared/model/jeu.model';

describe('Component Tests', () => {
  describe('Jeu Management Component', () => {
    let comp: JeuComponent;
    let fixture: ComponentFixture<JeuComponent>;
    let service: JeuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JeuxvideosTestModule],
        declarations: [JeuComponent],
      })
        .overrideTemplate(JeuComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JeuComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JeuService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Jeu(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.jeus && comp.jeus[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
