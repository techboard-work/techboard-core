import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TechConfigurationComponent } from './list/tech-configuration.component';
import { TechConfigurationDetailComponent } from './detail/tech-configuration-detail.component';
import { TechConfigurationUpdateComponent } from './update/tech-configuration-update.component';
import { TechConfigurationDeleteDialogComponent } from './delete/tech-configuration-delete-dialog.component';
import { TechConfigurationRoutingModule } from './route/tech-configuration-routing.module';

@NgModule({
  imports: [SharedModule, TechConfigurationRoutingModule],
  declarations: [
    TechConfigurationComponent,
    TechConfigurationDetailComponent,
    TechConfigurationUpdateComponent,
    TechConfigurationDeleteDialogComponent,
  ],
})
export class TechConfigurationModule {}
