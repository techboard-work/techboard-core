import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActivityKind } from '../activity-kind.model';
import { ActivityKindService } from '../service/activity-kind.service';

@Injectable({ providedIn: 'root' })
export class ActivityKindRoutingResolveService implements Resolve<IActivityKind | null> {
  constructor(protected service: ActivityKindService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActivityKind | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((activityKind: HttpResponse<IActivityKind>) => {
          if (activityKind.body) {
            return of(activityKind.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
