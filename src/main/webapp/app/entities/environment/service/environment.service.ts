import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnvironment, NewEnvironment } from '../environment.model';

export type PartialUpdateEnvironment = Partial<IEnvironment> & Pick<IEnvironment, 'id'>;

export type EntityResponseType = HttpResponse<IEnvironment>;
export type EntityArrayResponseType = HttpResponse<IEnvironment[]>;

@Injectable({ providedIn: 'root' })
export class EnvironmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/environments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(environment: NewEnvironment): Observable<EntityResponseType> {
    return this.http.post<IEnvironment>(this.resourceUrl, environment, { observe: 'response' });
  }

  update(environment: IEnvironment): Observable<EntityResponseType> {
    return this.http.put<IEnvironment>(`${this.resourceUrl}/${this.getEnvironmentIdentifier(environment)}`, environment, {
      observe: 'response',
    });
  }

  partialUpdate(environment: PartialUpdateEnvironment): Observable<EntityResponseType> {
    return this.http.patch<IEnvironment>(`${this.resourceUrl}/${this.getEnvironmentIdentifier(environment)}`, environment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnvironment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnvironment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnvironmentIdentifier(environment: Pick<IEnvironment, 'id'>): number {
    return environment.id;
  }

  compareEnvironment(o1: Pick<IEnvironment, 'id'> | null, o2: Pick<IEnvironment, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnvironmentIdentifier(o1) === this.getEnvironmentIdentifier(o2) : o1 === o2;
  }

  addEnvironmentToCollectionIfMissing<Type extends Pick<IEnvironment, 'id'>>(
    environmentCollection: Type[],
    ...environmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const environments: Type[] = environmentsToCheck.filter(isPresent);
    if (environments.length > 0) {
      const environmentCollectionIdentifiers = environmentCollection.map(
        environmentItem => this.getEnvironmentIdentifier(environmentItem)!
      );
      const environmentsToAdd = environments.filter(environmentItem => {
        const environmentIdentifier = this.getEnvironmentIdentifier(environmentItem);
        if (environmentCollectionIdentifiers.includes(environmentIdentifier)) {
          return false;
        }
        environmentCollectionIdentifiers.push(environmentIdentifier);
        return true;
      });
      return [...environmentsToAdd, ...environmentCollection];
    }
    return environmentCollection;
  }

  getEnvironments(): Observable<any> {
    return this.http.get(this.resourceUrl);
  }
}
