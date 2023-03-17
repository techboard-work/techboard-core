import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnvironment } from '../environment.model';
import { EnvironmentService } from '../service/environment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './environment-delete-dialog.component.html',
})
export class EnvironmentDeleteDialogComponent {
  environment?: IEnvironment;

  constructor(protected environmentService: EnvironmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.environmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
