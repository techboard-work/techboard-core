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
        path: 'tag',
        data: { pageTitle: 'techboardApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'techboardApp.activity.home.title' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
