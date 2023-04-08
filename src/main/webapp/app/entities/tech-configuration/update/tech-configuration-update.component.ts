import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TechConfigurationFormService, TechConfigurationFormGroup } from './tech-configuration-form.service';
import { ITechConfiguration } from '../tech-configuration.model';
import { TechConfigurationService } from '../service/tech-configuration.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-tech-configuration-update',
  templateUrl: './tech-configuration-update.component.html',
})
export class TechConfigurationUpdateComponent implements OnInit {
  isSaving = false;
  techConfiguration: ITechConfiguration | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: TechConfigurationFormGroup = this.techConfigurationFormService.createTechConfigurationFormGroup();

  constructor(
    protected techConfigurationService: TechConfigurationService,
    protected techConfigurationFormService: TechConfigurationFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techConfiguration }) => {
      this.techConfiguration = techConfiguration;
      if (techConfiguration) {
        this.updateForm(techConfiguration);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techConfiguration = this.techConfigurationFormService.getTechConfiguration(this.editForm);
    if (techConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.techConfigurationService.update(techConfiguration));
    } else {
      this.subscribeToSaveResponse(this.techConfigurationService.create(techConfiguration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechConfiguration>>): void {
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

  protected updateForm(techConfiguration: ITechConfiguration): void {
    this.techConfiguration = techConfiguration;
    this.techConfigurationFormService.resetForm(this.editForm, techConfiguration);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, techConfiguration.author);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.techConfiguration?.author)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
