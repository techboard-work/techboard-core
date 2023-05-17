import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { dashboardRoutes } from './dashboard.route';
import { DashboardComponent } from './dashboard.component';
import { ActivitiesComponent } from './activities/activities.component';
import { EnvironmentComponent } from './environment/environment.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(dashboardRoutes)],
  declarations: [DashboardComponent, ActivitiesComponent, EnvironmentComponent],
})
export class DashboardModule {}
