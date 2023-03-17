import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IActivityKind, NewActivityKind } from '../activity-kind.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IActivityKind for edit and NewActivityKindFormGroupInput for create.
 */
type ActivityKindFormGroupInput = IActivityKind | PartialWithRequiredKeyOf<NewActivityKind>;

type ActivityKindFormDefaults = Pick<NewActivityKind, 'id'>;

type ActivityKindFormGroupContent = {
  id: FormControl<IActivityKind['id'] | NewActivityKind['id']>;
  name: FormControl<IActivityKind['name']>;
  description: FormControl<IActivityKind['description']>;
  color: FormControl<IActivityKind['color']>;
  icon: FormControl<IActivityKind['icon']>;
};

export type ActivityKindFormGroup = FormGroup<ActivityKindFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ActivityKindFormService {
  createActivityKindFormGroup(activityKind: ActivityKindFormGroupInput = { id: null }): ActivityKindFormGroup {
    const activityKindRawValue = {
      ...this.getFormDefaults(),
      ...activityKind,
    };
    return new FormGroup<ActivityKindFormGroupContent>({
      id: new FormControl(
        { value: activityKindRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(activityKindRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(activityKindRawValue.description, {
        validators: [Validators.required],
      }),
      color: new FormControl(activityKindRawValue.color, {
        validators: [Validators.required],
      }),
      icon: new FormControl(activityKindRawValue.icon),
    });
  }

  getActivityKind(form: ActivityKindFormGroup): IActivityKind | NewActivityKind {
    return form.getRawValue() as IActivityKind | NewActivityKind;
  }

  resetForm(form: ActivityKindFormGroup, activityKind: ActivityKindFormGroupInput): void {
    const activityKindRawValue = { ...this.getFormDefaults(), ...activityKind };
    form.reset(
      {
        ...activityKindRawValue,
        id: { value: activityKindRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ActivityKindFormDefaults {
    return {
      id: null,
    };
  }
}
