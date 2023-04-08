import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface ITechConfiguration {
  id: number;
  version?: number | null;
  timestamp?: dayjs.Dayjs | null;
  content?: string | null;
  author?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewTechConfiguration = Omit<ITechConfiguration, 'id'> & { id: null };
