import { IActivity } from 'app/entities/activity/activity.model';

export interface ITag {
  id: number;
  tag?: string | null;
  order?: number | null;
  color?: string | null;
  active?: boolean | null;
  description?: string | null;
  icon?: string | null;
  link?: string | null;
  activities?: Pick<IActivity, 'id'>[] | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
