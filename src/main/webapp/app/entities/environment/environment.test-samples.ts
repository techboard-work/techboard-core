import { IEnvironment, NewEnvironment } from './environment.model';

export const sampleWithRequiredData: IEnvironment = {
  id: 87640,
  name: 'AWS prod',
  label: 'Production in Amazon',
  description: 'The big and long description',
  color: 'cyan',
  level: 0,
};

export const sampleWithPartialData: IEnvironment = {
  id: 44805,
  name: 'OCI prod',
  label: 'Production in Oracle',
  description: 'The regional deployment',
  color: 'ivory',
  level: 0,
  link: 'Identity',
};

export const sampleWithFullData: IEnvironment = {
  id: 97229,
  name: 'UAT',
  label: 'Pre-prod in AWS',
  description: 'Collaboration',
  color: 'yellow',
  level: 1,
  link: 'BCEAO JBOD array',
};

export const sampleWithNewData: NewEnvironment = {
  name: 'Bike Steel',
  label: 'Technician',
  description: 'National Ball',
  color: 'tan',
  level: 2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
