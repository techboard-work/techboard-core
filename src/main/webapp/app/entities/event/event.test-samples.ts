import dayjs from 'dayjs/esm';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
  message: 'Cotton',
  receivedOn: dayjs('2023-04-04T23:14'),
};

export const sampleWithPartialData: IEvent = {
  id: 2671,
  message: 'red',
  receivedOn: dayjs('2023-04-04T18:01'),
};

export const sampleWithFullData: IEvent = {
  id: 58638,
  message: 'Metal teal',
  receivedOn: dayjs('2023-04-04T19:02'),
  link: 'Account',
};

export const sampleWithNewData: NewEvent = {
  message: 'Personal deposit sky',
  receivedOn: dayjs('2023-04-04T16:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
