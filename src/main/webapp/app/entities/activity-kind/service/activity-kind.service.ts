import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivityKind, NewActivityKind } from '../activity-kind.model';

export type PartialUpdateActivityKind = Partial<IActivityKind> & Pick<IActivityKind, 'id'>;

export type EntityResponseType = HttpResponse<IActivityKind>;
export type EntityArrayResponseType = HttpResponse<IActivityKind[]>;

@Injectable({ providedIn: 'root' })
export class ActivityKindService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activity-kinds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(activityKind: NewActivityKind): Observable<EntityResponseType> {
    return this.http.post<IActivityKind>(this.resourceUrl, activityKind, { observe: 'response' });
  }

  update(activityKind: IActivityKind): Observable<EntityResponseType> {
    return this.http.put<IActivityKind>(`${this.resourceUrl}/${this.getActivityKindIdentifier(activityKind)}`, activityKind, {
      observe: 'response',
    });
  }

  partialUpdate(activityKind: PartialUpdateActivityKind): Observable<EntityResponseType> {
    return this.http.patch<IActivityKind>(`${this.resourceUrl}/${this.getActivityKindIdentifier(activityKind)}`, activityKind, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActivityKind>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActivityKind[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getActivityKindIdentifier(activityKind: Pick<IActivityKind, 'id'>): number {
    return activityKind.id;
  }

  compareActivityKind(o1: Pick<IActivityKind, 'id'> | null, o2: Pick<IActivityKind, 'id'> | null): boolean {
    return o1 && o2 ? this.getActivityKindIdentifier(o1) === this.getActivityKindIdentifier(o2) : o1 === o2;
  }

  addActivityKindToCollectionIfMissing<Type extends Pick<IActivityKind, 'id'>>(
    activityKindCollection: Type[],
    ...activityKindsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const activityKinds: Type[] = activityKindsToCheck.filter(isPresent);
    if (activityKinds.length > 0) {
      const activityKindCollectionIdentifiers = activityKindCollection.map(
        activityKindItem => this.getActivityKindIdentifier(activityKindItem)!
      );
      const activityKindsToAdd = activityKinds.filter(activityKindItem => {
        const activityKindIdentifier = this.getActivityKindIdentifier(activityKindItem);
        if (activityKindCollectionIdentifiers.includes(activityKindIdentifier)) {
          return false;
        }
        activityKindCollectionIdentifiers.push(activityKindIdentifier);
        return true;
      });
      return [...activityKindsToAdd, ...activityKindCollection];
    }
    return activityKindCollection;
  }
}
