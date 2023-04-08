import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechConfiguration } from '../tech-configuration.model';
import { TechConfigurationService } from '../service/tech-configuration.service';

@Injectable({ providedIn: 'root' })
export class TechConfigurationRoutingResolveService implements Resolve<ITechConfiguration | null> {
  constructor(protected service: TechConfigurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITechConfiguration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((techConfiguration: HttpResponse<ITechConfiguration>) => {
          if (techConfiguration.body) {
            return of(techConfiguration.body);
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
