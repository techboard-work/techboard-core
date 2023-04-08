import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TechConfigurationComponent } from '../list/tech-configuration.component';
import { TechConfigurationDetailComponent } from '../detail/tech-configuration-detail.component';
import { TechConfigurationUpdateComponent } from '../update/tech-configuration-update.component';
import { TechConfigurationRoutingResolveService } from './tech-configuration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const techConfigurationRoute: Routes = [
  {
    path: '',
    component: TechConfigurationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechConfigurationDetailComponent,
    resolve: {
      techConfiguration: TechConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechConfigurationUpdateComponent,
    resolve: {
      techConfiguration: TechConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechConfigurationUpdateComponent,
    resolve: {
      techConfiguration: TechConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(techConfigurationRoute)],
  exports: [RouterModule],
})
export class TechConfigurationRoutingModule {}
