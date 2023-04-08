import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IActivity, NewActivity } from '../activity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IActivity for edit and NewActivityFormGroupInput for create.
 */
type ActivityFormGroupInput = IActivity | PartialWithRequiredKeyOf<NewActivity>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IActivity | NewActivity> = Omit<T, 'startedOn' | 'finishedOn'> & {
  startedOn?: string | null;
  finishedOn?: string | null;
};

type ActivityFormRawValue = FormValueOf<IActivity>;

type NewActivityFormRawValue = FormValueOf<NewActivity>;

type ActivityFormDefaults = Pick<NewActivity, 'id' | 'startedOn' | 'finishedOn'>;

type ActivityFormGroupContent = {
  id: FormControl<ActivityFormRawValue['id'] | NewActivity['id']>;
  name: FormControl<ActivityFormRawValue['name']>;
  startedOn: FormControl<ActivityFormRawValue['startedOn']>;
  finishedOn: FormControl<ActivityFormRawValue['finishedOn']>;
  link: FormControl<ActivityFormRawValue['link']>;
  severity: FormControl<ActivityFormRawValue['severity']>;
  environment: FormControl<ActivityFormRawValue['environment']>;
  kind: FormControl<ActivityFormRawValue['kind']>;
  owner: FormControl<ActivityFormRawValue['owner']>;
};

export type ActivityFormGroup = FormGroup<ActivityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ActivityFormService {
  createActivityFormGroup(activity: ActivityFormGroupInput = { id: null }): ActivityFormGroup {
    const activityRawValue = this.convertActivityToActivityRawValue({
      ...this.getFormDefaults(),
      ...activity,
    });
    return new FormGroup<ActivityFormGroupContent>({
      id: new FormControl(
        { value: activityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(activityRawValue.name, {
        validators: [Validators.required],
      }),
      startedOn: new FormControl(activityRawValue.startedOn, {
        validators: [Validators.required],
      }),
      finishedOn: new FormControl(activityRawValue.finishedOn),
      link: new FormControl(activityRawValue.link),
      severity: new FormControl(activityRawValue.severity, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      environment: new FormControl(activityRawValue.environment),
      kind: new FormControl(activityRawValue.kind),
      owner: new FormControl(activityRawValue.owner),
    });
  }

  getActivity(form: ActivityFormGroup): IActivity | NewActivity {
    return this.convertActivityRawValueToActivity(form.getRawValue() as ActivityFormRawValue | NewActivityFormRawValue);
  }

  resetForm(form: ActivityFormGroup, activity: ActivityFormGroupInput): void {
    const activityRawValue = this.convertActivityToActivityRawValue({ ...this.getFormDefaults(), ...activity });
    form.reset(
      {
        ...activityRawValue,
        id: { value: activityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ActivityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startedOn: currentTime,
      finishedOn: currentTime,
    };
  }

  private convertActivityRawValueToActivity(rawActivity: ActivityFormRawValue | NewActivityFormRawValue): IActivity | NewActivity {
    return {
      ...rawActivity,
      startedOn: dayjs(rawActivity.startedOn, DATE_TIME_FORMAT),
      finishedOn: dayjs(rawActivity.finishedOn, DATE_TIME_FORMAT),
    };
  }

  private convertActivityToActivityRawValue(
    activity: IActivity | (Partial<NewActivity> & ActivityFormDefaults)
  ): ActivityFormRawValue | PartialWithRequiredKeyOf<NewActivityFormRawValue> {
    return {
      ...activity,
      startedOn: activity.startedOn ? activity.startedOn.format(DATE_TIME_FORMAT) : undefined,
      finishedOn: activity.finishedOn ? activity.finishedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
