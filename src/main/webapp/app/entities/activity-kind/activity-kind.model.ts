export interface IActivityKind {
  id: number;
  name?: string | null;
  description?: string | null;
  color?: string | null;
  icon?: string | null;
}

export type NewActivityKind = Omit<IActivityKind, 'id'> & { id: null };
