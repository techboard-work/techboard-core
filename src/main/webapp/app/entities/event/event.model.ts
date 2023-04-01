import dayjs from 'dayjs/esm';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { IUser } from 'app/entities/user/user.model';

export interface IEvent {
  id: number;
  message?: string | null;
  receivedOn?: dayjs.Dayjs | null;
  link?: string | null;
  environment?: Pick<IEnvironment, 'id'> | null;
  reporter?: Pick<IUser, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
