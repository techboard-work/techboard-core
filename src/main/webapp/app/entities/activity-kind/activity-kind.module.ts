import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActivityKindComponent } from './list/activity-kind.component';
import { ActivityKindDetailComponent } from './detail/activity-kind-detail.component';
import { ActivityKindUpdateComponent } from './update/activity-kind-update.component';
import { ActivityKindDeleteDialogComponent } from './delete/activity-kind-delete-dialog.component';
import { ActivityKindRoutingModule } from './route/activity-kind-routing.module';

@NgModule({
  imports: [SharedModule, ActivityKindRoutingModule],
  declarations: [ActivityKindComponent, ActivityKindDetailComponent, ActivityKindUpdateComponent, ActivityKindDeleteDialogComponent],
})
export class ActivityKindModule {}
