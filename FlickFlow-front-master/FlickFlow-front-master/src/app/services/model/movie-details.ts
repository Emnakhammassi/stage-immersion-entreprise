export interface MovieDetails {
  adult: boolean;
  backdropPath: string; // Change to camelCase
  belongsToCollection: BelongsToCollection; // Change to camelCase
  budget: number;
  genres: Genre[];
  homepage: string;
  id: number;
  imdbId: string; // Change to camelCase
  originalLanguage: string; // Change to camelCase
  originalTitle: string; // Change to camelCase
  overview: string;
  popularity: number;
  posterPath: string; // Change to camelCase
  productionCompanies: ProductionCompany[]; // Change to camelCase
  productionCountries: ProductionCountry[]; // Change to camelCase
  releaseDate: string; // Change to camelCase
  revenue: number;
  runtime: number;
  spokenLanguages: SpokenLanguage[]; // Change to camelCase
  status: string;
  tagline: string;
  title: string;
  video: boolean;
  voteAverage: number; // Change to camelCase
  voteCount: number; // Change to camelCase
}

export interface BelongsToCollection {
  id: number;
  name: string;
  posterPath: string; // Change to camelCase
  backdropPath: string; // Change to camelCase
}

export interface Genre {
  id: number;
  name: string;
}

export interface ProductionCompany {
  id: number;
  logoPath: string; // Change to camelCase
  name: string;
  originCountry: string; // Change to camelCase
}

export interface ProductionCountry {
  iso31661: string; // Change to camelCase
  name: string;
}

export interface SpokenLanguage {
  englishName: string; // Change to camelCase
  iso6391: string; // Change to camelCase
  name: string;
}
