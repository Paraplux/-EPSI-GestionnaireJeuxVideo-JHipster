import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPlateforme } from 'app/shared/model/plateforme.model';

type EntityResponseType = HttpResponse<IPlateforme>;
type EntityArrayResponseType = HttpResponse<IPlateforme[]>;

@Injectable({ providedIn: 'root' })
export class PlateformeService {
  public resourceUrl = SERVER_API_URL + 'api/plateformes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/plateformes';

  constructor(protected http: HttpClient) {}

  create(plateforme: IPlateforme): Observable<EntityResponseType> {
    return this.http.post<IPlateforme>(this.resourceUrl, plateforme, { observe: 'response' });
  }

  update(plateforme: IPlateforme): Observable<EntityResponseType> {
    return this.http.put<IPlateforme>(this.resourceUrl, plateforme, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlateforme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlateforme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlateforme[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
