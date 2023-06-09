import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { dashboardRoutes } from './dashboard.route';
import { DashboardComponent } from './dashboard.component';
import { ActivitiesComponent } from './activities/activities.component';
import { EnvironmentComponent } from './environment/environment.component';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { AddActivityModalComponent } from './add-activity-modal/add-activity-modal.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(dashboardRoutes), NgbDatepickerModule],
  declarations: [DashboardComponent, ActivitiesComponent, EnvironmentComponent, AddActivityModalComponent],
})
export class DashboardModule {}
