import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 42372,
  tag: 'human-resource',
  order: 72152,
  color: 'azure',
  active: true,
};

export const sampleWithPartialData: ITag = {
  id: 47389,
  tag: 'deposit intangible',
  order: 1687,
  color: 'salmon',
  active: true,
  icon: 'withdrawal Tools Fresh',
};

export const sampleWithFullData: ITag = {
  id: 31463,
  tag: 'salmon',
  order: 78588,
  color: 'gold',
  active: false,
  description: 'Fish technologies',
  icon: 'Grocery XML needs-based',
  link: 'efficient',
};

export const sampleWithNewData: NewTag = {
  tag: 'Denmark capacity',
  order: 3512,
  color: 'black',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
