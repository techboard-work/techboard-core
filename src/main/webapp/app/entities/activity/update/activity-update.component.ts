import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ActivityFormService, ActivityFormGroup } from './activity-form.service';
import { IActivity } from '../activity.model';
import { ActivityService } from '../service/activity.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { EnvironmentService } from 'app/entities/environment/service/environment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-activity-update',
  templateUrl: './activity-update.component.html',
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;
  activity: IActivity | null = null;

  tagsSharedCollection: ITag[] = [];
  environmentsSharedCollection: IEnvironment[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ActivityFormGroup = this.activityFormService.createActivityFormGroup();

  constructor(
    protected activityService: ActivityService,
    protected activityFormService: ActivityFormService,
    protected tagService: TagService,
    protected environmentService: EnvironmentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  compareEnvironment = (o1: IEnvironment | null, o2: IEnvironment | null): boolean => this.environmentService.compareEnvironment(o1, o2);

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

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, ...(activity.tags ?? []));
    this.environmentsSharedCollection = this.environmentService.addEnvironmentToCollectionIfMissing<IEnvironment>(
      this.environmentsSharedCollection,
      activity.environment
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, activity.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, ...(this.activity?.tags ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

    this.environmentService
      .query()
      .pipe(map((res: HttpResponse<IEnvironment[]>) => res.body ?? []))
      .pipe(
        map((environments: IEnvironment[]) =>
          this.environmentService.addEnvironmentToCollectionIfMissing<IEnvironment>(environments, this.activity?.environment)
        )
      )
      .subscribe((environments: IEnvironment[]) => (this.environmentsSharedCollection = environments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.activity?.owner)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
