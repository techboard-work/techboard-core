export interface IEnvironment {
  id: number;
  name?: string | null;
  label?: string | null;
  description?: string | null;
  color?: string | null;
  level?: number | null;
  link?: string | null;
}

export type NewEnvironment = Omit<IEnvironment, 'id'> & { id: null };
