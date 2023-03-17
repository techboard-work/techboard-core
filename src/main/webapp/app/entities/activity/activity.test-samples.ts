import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 68021,
  name: 'Marketing Principal',
  startedOn: dayjs('2023-03-16T22:10'),
};

export const sampleWithPartialData: IActivity = {
  id: 94774,
  name: 'primary Sausages',
  startedOn: dayjs('2023-03-17T05:32'),
  finishedOn: dayjs('2023-03-17T09:09'),
  impediment: true,
};

export const sampleWithFullData: IActivity = {
  id: 90211,
  name: 'open-source',
  startedOn: dayjs('2023-03-17T02:18'),
  finishedOn: dayjs('2023-03-17T01:57'),
  link: 'connect',
  impediment: true,
};

export const sampleWithNewData: NewActivity = {
  name: 'backing',
  startedOn: dayjs('2023-03-17T04:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
