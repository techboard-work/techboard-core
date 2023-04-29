import { IEnvironment, NewEnvironment } from './environment.model';

export const sampleWithRequiredData: IEnvironment = {
  id: 87640,
  name: 'Chair withdrawal',
  code: 'Soft proactive Nebraska',
  color: 'olive',
  level: 85990,
};

export const sampleWithPartialData: IEnvironment = {
  id: 23913,
  name: 'Colorado Pizza',
  code: 'indigo Underpass',
  color: 'white',
  level: 99862,
};

export const sampleWithFullData: IEnvironment = {
  id: 64768,
  name: 'Avon eyeballs Multi-channelled',
  code: 'Tasty invoice',
  color: 'olive',
  level: 45141,
  link: 'Ergonomic Borders orchid',
};

export const sampleWithNewData: NewEnvironment = {
  name: 'Jewelery',
  code: 'Handmade',
  color: 'magenta',
  level: 93921,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
