import dayjs from 'dayjs/esm';

import { ITechConfiguration, NewTechConfiguration } from './tech-configuration.model';

export const sampleWithRequiredData: ITechConfiguration = {
  id: 4098,
  version: 906229,
  timestamp: dayjs('2023-04-10T15:17'),
  content: 'Berkshire Berkshire',
};

export const sampleWithPartialData: ITechConfiguration = {
  id: 29764,
  version: 975974,
  timestamp: dayjs('2023-04-11T05:59'),
  content: 'Saint',
};

export const sampleWithFullData: ITechConfiguration = {
  id: 31955,
  version: 937692,
  timestamp: dayjs('2023-04-11T07:17'),
  content: 'connecting Borders',
};

export const sampleWithNewData: NewTechConfiguration = {
  version: 142463,
  timestamp: dayjs('2023-04-10T16:47'),
  content: 'optical',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
