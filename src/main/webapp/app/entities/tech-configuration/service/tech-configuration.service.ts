import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechConfiguration, NewTechConfiguration } from '../tech-configuration.model';

export type PartialUpdateTechConfiguration = Partial<ITechConfiguration> & Pick<ITechConfiguration, 'id'>;

type RestOf<T extends ITechConfiguration | NewTechConfiguration> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestTechConfiguration = RestOf<ITechConfiguration>;

export type NewRestTechConfiguration = RestOf<NewTechConfiguration>;

export type PartialUpdateRestTechConfiguration = RestOf<PartialUpdateTechConfiguration>;

export type EntityResponseType = HttpResponse<ITechConfiguration>;
export type EntityArrayResponseType = HttpResponse<ITechConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class TechConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-configurations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(techConfiguration: NewTechConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(techConfiguration);
    return this.http
      .post<RestTechConfiguration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(techConfiguration: ITechConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(techConfiguration);
    return this.http
      .put<RestTechConfiguration>(`${this.resourceUrl}/${this.getTechConfigurationIdentifier(techConfiguration)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(techConfiguration: PartialUpdateTechConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(techConfiguration);
    return this.http
      .patch<RestTechConfiguration>(`${this.resourceUrl}/${this.getTechConfigurationIdentifier(techConfiguration)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTechConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTechConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechConfigurationIdentifier(techConfiguration: Pick<ITechConfiguration, 'id'>): number {
    return techConfiguration.id;
  }

  compareTechConfiguration(o1: Pick<ITechConfiguration, 'id'> | null, o2: Pick<ITechConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechConfigurationIdentifier(o1) === this.getTechConfigurationIdentifier(o2) : o1 === o2;
  }

  addTechConfigurationToCollectionIfMissing<Type extends Pick<ITechConfiguration, 'id'>>(
    techConfigurationCollection: Type[],
    ...techConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techConfigurations: Type[] = techConfigurationsToCheck.filter(isPresent);
    if (techConfigurations.length > 0) {
      const techConfigurationCollectionIdentifiers = techConfigurationCollection.map(
        techConfigurationItem => this.getTechConfigurationIdentifier(techConfigurationItem)!
      );
      const techConfigurationsToAdd = techConfigurations.filter(techConfigurationItem => {
        const techConfigurationIdentifier = this.getTechConfigurationIdentifier(techConfigurationItem);
        if (techConfigurationCollectionIdentifiers.includes(techConfigurationIdentifier)) {
          return false;
        }
        techConfigurationCollectionIdentifiers.push(techConfigurationIdentifier);
        return true;
      });
      return [...techConfigurationsToAdd, ...techConfigurationCollection];
    }
    return techConfigurationCollection;
  }

  protected convertDateFromClient<T extends ITechConfiguration | NewTechConfiguration | PartialUpdateTechConfiguration>(
    techConfiguration: T
  ): RestOf<T> {
    return {
      ...techConfiguration,
      timestamp: techConfiguration.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTechConfiguration: RestTechConfiguration): ITechConfiguration {
    return {
      ...restTechConfiguration,
      timestamp: restTechConfiguration.timestamp ? dayjs(restTechConfiguration.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTechConfiguration>): HttpResponse<ITechConfiguration> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTechConfiguration[]>): HttpResponse<ITechConfiguration[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
