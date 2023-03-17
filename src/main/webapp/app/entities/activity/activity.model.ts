import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { IActivityKind } from 'app/entities/activity-kind/activity-kind.model';

export interface IActivity {
  id: number;
  name?: string | null;
  startedOn?: dayjs.Dayjs | null;
  finishedOn?: dayjs.Dayjs | null;
  link?: string | null;
  impediment?: boolean | null;
  employee?: Pick<IEmployee, 'id'> | null;
  environment?: Pick<IEnvironment, 'id'> | null;
  kind?: Pick<IActivityKind, 'id'> | null;
}

export type NewActivity = Omit<IActivity, 'id'> & { id: null };
