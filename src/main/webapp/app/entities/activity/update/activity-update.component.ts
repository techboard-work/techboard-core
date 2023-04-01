import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ActivityFormService, ActivityFormGroup } from './activity-form.service';
import { IActivity } from '../activity.model';
import { ActivityService } from '../service/activity.service';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { EnvironmentService } from 'app/entities/environment/service/environment.service';
import { IActivityKind } from 'app/entities/activity-kind/activity-kind.model';
import { ActivityKindService } from 'app/entities/activity-kind/service/activity-kind.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-activity-update',
  templateUrl: './activity-update.component.html',
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;
  activity: IActivity | null = null;

  environmentsSharedCollection: IEnvironment[] = [];
  activityKindsSharedCollection: IActivityKind[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ActivityFormGroup = this.activityFormService.createActivityFormGroup();

  constructor(
    protected activityService: ActivityService,
    protected activityFormService: ActivityFormService,
    protected environmentService: EnvironmentService,
    protected activityKindService: ActivityKindService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEnvironment = (o1: IEnvironment | null, o2: IEnvironment | null): boolean => this.environmentService.compareEnvironment(o1, o2);

  compareActivityKind = (o1: IActivityKind | null, o2: IActivityKind | null): boolean =>
    this.activityKindService.compareActivityKind(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.activity = activity;
      if (activity) {
        this.updateForm(activity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.activityFormService.getActivity(this.editForm);
    if (activity.id !== null) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivity>>): void {
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

  protected updateForm(activity: IActivity): void {
    this.activity = activity;
    this.activityFormService.resetForm(this.editForm, activity);

    this.environmentsSharedCollection = this.environmentService.addEnvironmentToCollectionIfMissing<IEnvironment>(
      this.environmentsSharedCollection,
      activity.environment
    );
    this.activityKindsSharedCollection = this.activityKindService.addActivityKindToCollectionIfMissing<IActivityKind>(
      this.activityKindsSharedCollection,
      activity.kind
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, activity.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.environmentService
      .query()
      .pipe(map((res: HttpResponse<IEnvironment[]>) => res.body ?? []))
      .pipe(
        map((environments: IEnvironment[]) =>
          this.environmentService.addEnvironmentToCollectionIfMissing<IEnvironment>(environments, this.activity?.environment)
        )
      )
      .subscribe((environments: IEnvironment[]) => (this.environmentsSharedCollection = environments));

    this.activityKindService
      .query()
      .pipe(map((res: HttpResponse<IActivityKind[]>) => res.body ?? []))
      .pipe(
        map((activityKinds: IActivityKind[]) =>
          this.activityKindService.addActivityKindToCollectionIfMissing<IActivityKind>(activityKinds, this.activity?.kind)
        )
      )
      .subscribe((activityKinds: IActivityKind[]) => (this.activityKindsSharedCollection = activityKinds));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.activity?.owner)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
