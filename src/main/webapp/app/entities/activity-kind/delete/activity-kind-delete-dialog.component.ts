import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivityKind } from '../activity-kind.model';
import { ActivityKindService } from '../service/activity-kind.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './activity-kind-delete-dialog.component.html',
})
export class ActivityKindDeleteDialogComponent {
  activityKind?: IActivityKind;

  constructor(protected activityKindService: ActivityKindService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activityKindService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
