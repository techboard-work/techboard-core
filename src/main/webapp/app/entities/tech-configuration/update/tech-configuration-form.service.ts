import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITechConfiguration, NewTechConfiguration } from '../tech-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechConfiguration for edit and NewTechConfigurationFormGroupInput for create.
 */
type TechConfigurationFormGroupInput = ITechConfiguration | PartialWithRequiredKeyOf<NewTechConfiguration>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITechConfiguration | NewTechConfiguration> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type TechConfigurationFormRawValue = FormValueOf<ITechConfiguration>;

type NewTechConfigurationFormRawValue = FormValueOf<NewTechConfiguration>;

type TechConfigurationFormDefaults = Pick<NewTechConfiguration, 'id' | 'timestamp'>;

type TechConfigurationFormGroupContent = {
  id: FormControl<TechConfigurationFormRawValue['id'] | NewTechConfiguration['id']>;
  version: FormControl<TechConfigurationFormRawValue['version']>;
  timestamp: FormControl<TechConfigurationFormRawValue['timestamp']>;
  content: FormControl<TechConfigurationFormRawValue['content']>;
  author: FormControl<TechConfigurationFormRawValue['author']>;
};

export type TechConfigurationFormGroup = FormGroup<TechConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechConfigurationFormService {
  createTechConfigurationFormGroup(techConfiguration: TechConfigurationFormGroupInput = { id: null }): TechConfigurationFormGroup {
    const techConfigurationRawValue = this.convertTechConfigurationToTechConfigurationRawValue({
      ...this.getFormDefaults(),
      ...techConfiguration,
    });
    return new FormGroup<TechConfigurationFormGroupContent>({
      id: new FormControl(
        { value: techConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      version: new FormControl(techConfigurationRawValue.version, {
        validators: [Validators.required, Validators.min(0), Validators.max(1000000)],
      }),
      timestamp: new FormControl(techConfigurationRawValue.timestamp, {
        validators: [Validators.required],
      }),
      content: new FormControl(techConfigurationRawValue.content, {
        validators: [Validators.required],
      }),
      author: new FormControl(techConfigurationRawValue.author),
    });
  }

  getTechConfiguration(form: TechConfigurationFormGroup): ITechConfiguration | NewTechConfiguration {
    return this.convertTechConfigurationRawValueToTechConfiguration(
      form.getRawValue() as TechConfigurationFormRawValue | NewTechConfigurationFormRawValue
    );
  }

  resetForm(form: TechConfigurationFormGroup, techConfiguration: TechConfigurationFormGroupInput): void {
    const techConfigurationRawValue = this.convertTechConfigurationToTechConfigurationRawValue({
      ...this.getFormDefaults(),
      ...techConfiguration,
    });
    form.reset(
      {
        ...techConfigurationRawValue,
        id: { value: techConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TechConfigurationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertTechConfigurationRawValueToTechConfiguration(
    rawTechConfiguration: TechConfigurationFormRawValue | NewTechConfigurationFormRawValue
  ): ITechConfiguration | NewTechConfiguration {
    return {
      ...rawTechConfiguration,
      timestamp: dayjs(rawTechConfiguration.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertTechConfigurationToTechConfigurationRawValue(
    techConfiguration: ITechConfiguration | (Partial<NewTechConfiguration> & TechConfigurationFormDefaults)
  ): TechConfigurationFormRawValue | PartialWithRequiredKeyOf<NewTechConfigurationFormRawValue> {
    return {
      ...techConfiguration,
      timestamp: techConfiguration.timestamp ? techConfiguration.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
