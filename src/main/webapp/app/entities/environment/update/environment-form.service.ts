import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnvironment, NewEnvironment } from '../environment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnvironment for edit and NewEnvironmentFormGroupInput for create.
 */
type EnvironmentFormGroupInput = IEnvironment | PartialWithRequiredKeyOf<NewEnvironment>;

type EnvironmentFormDefaults = Pick<NewEnvironment, 'id'>;

type EnvironmentFormGroupContent = {
  id: FormControl<IEnvironment['id'] | NewEnvironment['id']>;
  name: FormControl<IEnvironment['name']>;
  label: FormControl<IEnvironment['label']>;
  description: FormControl<IEnvironment['description']>;
  color: FormControl<IEnvironment['color']>;
  level: FormControl<IEnvironment['level']>;
  link: FormControl<IEnvironment['link']>;
};

export type EnvironmentFormGroup = FormGroup<EnvironmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnvironmentFormService {
  createEnvironmentFormGroup(environment: EnvironmentFormGroupInput = { id: null }): EnvironmentFormGroup {
    const environmentRawValue = {
      ...this.getFormDefaults(),
      ...environment,
    };
    return new FormGroup<EnvironmentFormGroupContent>({
      id: new FormControl(
        { value: environmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(environmentRawValue.name, {
        validators: [Validators.required],
      }),
      label: new FormControl(environmentRawValue.label, {
        validators: [Validators.required],
      }),
      description: new FormControl(environmentRawValue.description, {
        validators: [Validators.required],
      }),
      color: new FormControl(environmentRawValue.color, {
        validators: [Validators.required],
      }),
      level: new FormControl(environmentRawValue.level, {
        validators: [Validators.required],
      }),
      link: new FormControl(environmentRawValue.link),
    });
  }

  getEnvironment(form: EnvironmentFormGroup): IEnvironment | NewEnvironment {
    return form.getRawValue() as IEnvironment | NewEnvironment;
  }

  resetForm(form: EnvironmentFormGroup, environment: EnvironmentFormGroupInput): void {
    const environmentRawValue = { ...this.getFormDefaults(), ...environment };
    form.reset(
      {
        ...environmentRawValue,
        id: { value: environmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EnvironmentFormDefaults {
    return {
      id: null,
    };
  }
}
