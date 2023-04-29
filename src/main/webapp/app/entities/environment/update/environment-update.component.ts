import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EnvironmentFormService, EnvironmentFormGroup } from './environment-form.service';
import { IEnvironment } from '../environment.model';
import { EnvironmentService } from '../service/environment.service';

@Component({
  selector: 'jhi-environment-update',
  templateUrl: './environment-update.component.html',
})
export class EnvironmentUpdateComponent implements OnInit {
  isSaving = false;
  environment: IEnvironment | null = null;

  editForm: EnvironmentFormGroup = this.environmentFormService.createEnvironmentFormGroup();

  constructor(
    protected environmentService: EnvironmentService,
    protected environmentFormService: EnvironmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ environment }) => {
      this.environment = environment;
      if (environment) {
        this.updateForm(environment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const environment = this.environmentFormService.getEnvironment(this.editForm);
    if (environment.id !== null) {
      this.subscribeToSaveResponse(this.environmentService.update(environment));
    } else {
      this.subscribeToSaveResponse(this.environmentService.create(environment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnvironment>>): void {
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

  protected updateForm(environment: IEnvironment): void {
    this.environment = environment;
    this.environmentFormService.resetForm(this.editForm, environment);
  }
}
