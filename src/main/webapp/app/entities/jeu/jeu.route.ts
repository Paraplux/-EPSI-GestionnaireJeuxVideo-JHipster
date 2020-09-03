import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJeu, Jeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';
import { JeuComponent } from './jeu.component';
import { JeuDetailComponent } from './jeu-detail.component';
import { JeuUpdateComponent } from './jeu-update.component';

@Injectable({ providedIn: 'root' })
export class JeuResolve implements Resolve<IJeu> {
  constructor(private service: JeuService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJeu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((jeu: HttpResponse<Jeu>) => {
          if (jeu.body) {
            return of(jeu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Jeu());
  }
}

export const jeuRoute: Routes = [
  {
    path: '',
    component: JeuComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.jeu.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JeuDetailComponent,
    resolve: {
      jeu: JeuResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.jeu.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JeuUpdateComponent,
    resolve: {
      jeu: JeuResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.jeu.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JeuUpdateComponent,
    resolve: {
      jeu: JeuResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.jeu.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
