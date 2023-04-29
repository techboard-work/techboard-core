import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { IUser } from 'app/entities/user/user.model';

export interface IActivity {
  id: number;
  name?: string | null;
  startedOn?: dayjs.Dayjs | null;
  finishedOn?: dayjs.Dayjs | null;
  description?: string | null;
  link?: string | null;
  flagged?: boolean | null;
  tags?: Pick<ITag, 'id'>[] | null;
  environment?: Pick<IEnvironment, 'id'> | null;
  owner?: Pick<IUser, 'id'> | null;
}

export type NewActivity = Omit<IActivity, 'id'> & { id: null };
