import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITag, NewTag } from '../tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITag for edit and NewTagFormGroupInput for create.
 */
type TagFormGroupInput = ITag | PartialWithRequiredKeyOf<NewTag>;

type TagFormDefaults = Pick<NewTag, 'id' | 'active' | 'activities'>;

type TagFormGroupContent = {
  id: FormControl<ITag['id'] | NewTag['id']>;
  tag: FormControl<ITag['tag']>;
  order: FormControl<ITag['order']>;
  color: FormControl<ITag['color']>;
  active: FormControl<ITag['active']>;
  description: FormControl<ITag['description']>;
  icon: FormControl<ITag['icon']>;
  link: FormControl<ITag['link']>;
  activities: FormControl<ITag['activities']>;
};

export type TagFormGroup = FormGroup<TagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagFormService {
  createTagFormGroup(tag: TagFormGroupInput = { id: null }): TagFormGroup {
    const tagRawValue = {
      ...this.getFormDefaults(),
      ...tag,
    };
    return new FormGroup<TagFormGroupContent>({
      id: new FormControl(
        { value: tagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tag: new FormControl(tagRawValue.tag, {
        validators: [Validators.required],
      }),
      order: new FormControl(tagRawValue.order, {
        validators: [Validators.required],
      }),
      color: new FormControl(tagRawValue.color, {
        validators: [Validators.required],
      }),
      active: new FormControl(tagRawValue.active, {
        validators: [Validators.required],
      }),
      description: new FormControl(tagRawValue.description),
      icon: new FormControl(tagRawValue.icon),
      link: new FormControl(tagRawValue.link),
      activities: new FormControl(tagRawValue.activities ?? []),
    });
  }

  getTag(form: TagFormGroup): ITag | NewTag {
    return form.getRawValue() as ITag | NewTag;
  }

  resetForm(form: TagFormGroup, tag: TagFormGroupInput): void {
    const tagRawValue = { ...this.getFormDefaults(), ...tag };
    form.reset(
      {
        ...tagRawValue,
        id: { value: tagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TagFormDefaults {
    return {
      id: null,
      active: false,
      activities: [],
    };
  }
}
