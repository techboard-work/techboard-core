import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITechConfiguration } from '../tech-configuration.model';
import { TechConfigurationService } from '../service/tech-configuration.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tech-configuration-delete-dialog.component.html',
})
export class TechConfigurationDeleteDialogComponent {
  techConfiguration?: ITechConfiguration;

  constructor(protected techConfigurationService: TechConfigurationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
