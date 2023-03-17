import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 68021,
  name: 'Marketing Principal',
  startedOn: dayjs('2023-03-19T21:54'),
};

export const sampleWithPartialData: IActivity = {
  id: 94774,
  name: 'primary Sausages',
  startedOn: dayjs('2023-03-20T05:16'),
  finishedOn: dayjs('2023-03-20T08:53'),
  impediment: true,
};

export const sampleWithFullData: IActivity = {
  id: 90211,
  name: 'open-source',
  startedOn: dayjs('2023-03-20T02:02'),
  finishedOn: dayjs('2023-03-20T01:41'),
  link: 'connect',
  impediment: true,
};

export const sampleWithNewData: NewActivity = {
  name: 'backing',
  startedOn: dayjs('2023-03-20T03:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
