import { Routes } from '@angular/router';

import { DashboardComponent } from './dashboard.component';
import { EnvironmentComponent } from './environment/environment.component';

export const dashboardRoutes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      pageTitle: 'dashboard.title',
    },
  },
  {
    path: ':envId',
    component: EnvironmentComponent,
    data: {
      pageTitle: 'dashboard.environment',
    },
  },
];
