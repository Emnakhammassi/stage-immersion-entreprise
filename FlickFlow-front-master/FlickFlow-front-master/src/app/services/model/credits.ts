export interface Credits {
  id: number;
  cast: Cast[];
  crew: Cast[];
}

export interface Cast {
  adult: boolean;
  gender: number;
  id: number;
  knownForDepartment: string;
  name: string;
  originalName: string;
  popularity: number;
  profilePath: string | null;
  castId?: number;
  character?: string;
  creditId: string;
  order?: number;
}
export interface MovieCastResponse extends Array<Cast> {}


