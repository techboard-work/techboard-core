import dayjs from 'dayjs/esm';

import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 68021,
  name: 'Marketing Principal',
  startedOn: dayjs('2023-05-01T16:08'),
  flagged: true,
};

export const sampleWithPartialData: IActivity = {
  id: 49850,
  name: 'Handcrafted Interactions Fresh',
  startedOn: dayjs('2023-05-02T00:51'),
  description: 'reciprocal platforms Branch',
  link: 'backing',
  flagged: false,
};

export const sampleWithFullData: IActivity = {
  id: 82826,
  name: 'bluetooth Concrete',
  startedOn: dayjs('2023-05-01T22:21'),
  finishedOn: dayjs('2023-05-02T06:18'),
  description: 'invoice bandwidth',
  link: 'Cloned copy streamline',
  flagged: true,
};

export const sampleWithNewData: NewActivity = {
  name: 'Function-based Public-key haptic',
  startedOn: dayjs('2023-05-01T14:54'),
  flagged: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
