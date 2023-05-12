import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { dashboardRoute } from './dashboard.route';
import { DashboardComponent } from './dashboard.component';
import { ActivitiesComponent } from './activities/activities.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([dashboardRoute])],
  declarations: [DashboardComponent, ActivitiesComponent],
})
export class DashboardModule {}
