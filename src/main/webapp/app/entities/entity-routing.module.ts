import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'environment',
        data: { pageTitle: 'techboardApp.environment.home.title' },
        loadChildren: () => import('./environment/environment.module').then(m => m.EnvironmentModule),
      },
      {
        path: 'activity-kind',
        data: { pageTitle: 'techboardApp.activityKind.home.title' },
        loadChildren: () => import('./activity-kind/activity-kind.module').then(m => m.ActivityKindModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'techboardApp.activity.home.title' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'techboardApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
