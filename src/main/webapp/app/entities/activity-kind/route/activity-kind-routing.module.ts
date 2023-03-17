import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActivityKindComponent } from '../list/activity-kind.component';
import { ActivityKindDetailComponent } from '../detail/activity-kind-detail.component';
import { ActivityKindUpdateComponent } from '../update/activity-kind-update.component';
import { ActivityKindRoutingResolveService } from './activity-kind-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const activityKindRoute: Routes = [
  {
    path: '',
    component: ActivityKindComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityKindDetailComponent,
    resolve: {
      activityKind: ActivityKindRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivityKindUpdateComponent,
    resolve: {
      activityKind: ActivityKindRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivityKindUpdateComponent,
    resolve: {
      activityKind: ActivityKindRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(activityKindRoute)],
  exports: [RouterModule],
})
export class ActivityKindRoutingModule {}
