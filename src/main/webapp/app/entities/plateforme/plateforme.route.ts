import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlateforme, Plateforme } from 'app/shared/model/plateforme.model';
import { PlateformeService } from './plateforme.service';
import { PlateformeComponent } from './plateforme.component';
import { PlateformeDetailComponent } from './plateforme-detail.component';
import { PlateformeUpdateComponent } from './plateforme-update.component';

@Injectable({ providedIn: 'root' })
export class PlateformeResolve implements Resolve<IPlateforme> {
  constructor(private service: PlateformeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlateforme> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((plateforme: HttpResponse<Plateforme>) => {
          if (plateforme.body) {
            return of(plateforme.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plateforme());
  }
}

export const plateformeRoute: Routes = [
  {
    path: '',
    component: PlateformeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.plateforme.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlateformeDetailComponent,
    resolve: {
      plateforme: PlateformeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.plateforme.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlateformeUpdateComponent,
    resolve: {
      plateforme: PlateformeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.plateforme.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlateformeUpdateComponent,
    resolve: {
      plateforme: PlateformeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jeuxvideosApp.plateforme.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
