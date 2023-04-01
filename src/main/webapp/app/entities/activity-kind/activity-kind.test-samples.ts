import { IActivityKind, NewActivityKind } from './activity-kind.model';

export const sampleWithRequiredData: IActivityKind = {
  id: 82253,
  name: 'sensor',
  description: 'payment',
  color: 'teal',
};

export const sampleWithPartialData: IActivityKind = {
  id: 85891,
  name: '1080p Computer',
  description: 'structure',
  color: 'teal',
};

export const sampleWithFullData: IActivityKind = {
  id: 5008,
  name: 'neural payment',
  description: 'capacitor Generic',
  color: 'violet',
  icon: 'Concrete',
};

export const sampleWithNewData: NewActivityKind = {
  name: 'connect Hat ivory',
  description: 'Balboa Fantastic Internal',
  color: 'orange',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
