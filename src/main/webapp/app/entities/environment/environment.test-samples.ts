import { IEnvironment, NewEnvironment } from './environment.model';

export const sampleWithRequiredData: IEnvironment = {
  id: 87640,
  name: 'Chair withdrawal',
  label: 'Soft proactive Nebraska',
  description: 'TCP New Centers',
  color: 'cyan',
  level: 84724,
};

export const sampleWithPartialData: IEnvironment = {
  id: 44805,
  name: 'back-end',
  label: 'program payment Practical',
  description: 'white',
  color: 'ivory',
  level: 47949,
  link: 'Identity',
};

export const sampleWithFullData: IEnvironment = {
  id: 97229,
  name: 'Dollar Ergonomic',
  label: 'invoice Personal',
  description: 'cross-platform',
  color: 'yellow',
  level: 72372,
  link: 'BCEAO JBOD array',
};

export const sampleWithNewData: NewEnvironment = {
  name: 'Bike Steel',
  label: 'Technician',
  description: 'National Ball',
  color: 'tan',
  level: 76912,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
