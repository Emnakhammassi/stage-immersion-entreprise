export interface MovieApiResponse {
  page: number;
  results: Moviemodel[];
  total_pages: number;
  total_results: number;
  genreId: number;
}

export interface Moviemodel {
  adult: boolean;
  backdrop_path: string;
  id: number;
  title: string;
  original_language: string;
  original_title: string;
  overview: string;
  poster_path: string;
  media_type: string;
  genre_ids: number[];
  genre_names?: string[];
  popularity: number;
  release_date: string;
  video: boolean;
  vote_average: number;
  vote_count: number;
}

export interface Cast {
  id: number;
  name: string;
  character: string;
  profile_path: string;
}

export interface Trailer {
  id: string;
  key: string;
  name: string;
  site: string;
  type: string;
}
