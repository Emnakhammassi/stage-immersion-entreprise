export interface Movies {
  dates: Dates;
  page: number;
  results: Movie[];
  total_pages: number;
  total_results: number;
}

export interface Dates {
  maximum: Date;
  minimum: Date;
}

export interface Movie {
  adult: boolean;
  backdropPath: string;
  genreid: number[];
  tmbdId: number;
  original_language: OriginalLanguage;
  original_title: string;
  overview: string;
  popularity: number;
  posterPath: string;
  release_date: Date;
  movieName: string;
  video: boolean;
  vote_average: number;
  vote_count: number;
}

export enum OriginalLanguage {
  En = "en",
  Es = "es",
}
