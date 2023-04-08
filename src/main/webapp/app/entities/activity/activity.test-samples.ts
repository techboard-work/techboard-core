import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 68021,
  name: 'Marketing Principal',
  startedOn: dayjs('2023-04-04T21:14'),
  doNotDisturb: true,
  severity: 2,
};

export const sampleWithPartialData: IActivity = {
  id: 49850,
  name: 'Handcrafted Interactions Fresh',
  startedOn: dayjs('2023-04-05T05:56'),
  finishedOn: dayjs('2023-04-04T16:16'),
  link: 'index',
  doNotDisturb: false,
  severity: 77,
};

export const sampleWithFullData: IActivity = {
  id: 45701,
  name: 'driver',
  startedOn: dayjs('2023-04-05T11:09'),
  finishedOn: dayjs('2023-04-05T04:55'),
  link: 'navigating Money',
  doNotDisturb: false,
  severity: 22,
};

export const sampleWithNewData: NewActivity = {
  name: 'frictionless world-class Producer',
  startedOn: dayjs('2023-04-05T08:35'),
  doNotDisturb: true,
  severity: 24,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
