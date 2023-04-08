import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 68021,
  name: 'Marketing Principal',
  startedOn: dayjs('2023-04-04T21:14'),
  severity: 75,
};

export const sampleWithPartialData: IActivity = {
  id: 94774,
  name: 'primary Sausages',
  startedOn: dayjs('2023-04-05T04:36'),
  link: 'programming',
  severity: 80,
};

export const sampleWithFullData: IActivity = {
  id: 26841,
  name: 'content connect driver',
  startedOn: dayjs('2023-04-05T11:09'),
  finishedOn: dayjs('2023-04-05T04:55'),
  link: 'navigating Money',
  severity: 47,
};

export const sampleWithNewData: NewActivity = {
  name: 'FTP',
  startedOn: dayjs('2023-04-05T11:24'),
  severity: 37,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
