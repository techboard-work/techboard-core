import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ActivityKindFormService, ActivityKindFormGroup } from './activity-kind-form.service';
import { IActivityKind } from '../activity-kind.model';
import { ActivityKindService } from '../service/activity-kind.service';

@Component({
  selector: 'jhi-activity-kind-update',
  templateUrl: './activity-kind-update.component.html',
})
export class ActivityKindUpdateComponent implements OnInit {
  isSaving = false;
  activityKind: IActivityKind | null = null;

  editForm: ActivityKindFormGroup = this.activityKindFormService.createActivityKindFormGroup();

  constructor(
    protected activityKindService: ActivityKindService,
    protected activityKindFormService: ActivityKindFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activityKind }) => {
      this.activityKind = activityKind;
      if (activityKind) {
        this.updateForm(activityKind);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activityKind = this.activityKindFormService.getActivityKind(this.editForm);
    if (activityKind.id !== null) {
      this.subscribeToSaveResponse(this.activityKindService.update(activityKind));
    } else {
      this.subscribeToSaveResponse(this.activityKindService.create(activityKind));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivityKind>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(activityKind: IActivityKind): void {
    this.activityKind = activityKind;
    this.activityKindFormService.resetForm(this.editForm, activityKind);
  }
}
