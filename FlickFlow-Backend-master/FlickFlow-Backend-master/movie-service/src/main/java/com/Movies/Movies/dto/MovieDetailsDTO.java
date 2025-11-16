// file: src/main/java/com/Movies/Movies/dto/MovieDetailsDTO.java
package com.Movies.Movies.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDetailsDTO {
    private boolean adult;
    private String backdropPath;
    private CollectionDetailsDTO belongsToCollection;
    private long budget;
    private List<GenreDTO> genres;
    private String homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private List<ProductionCompanyDTO> productionCompanies;
    private List<ProductionCountryDTO> productionCountries;
    private String releaseDate;
    private long revenue;
    private int runtime;
    private List<SpokenLanguageDTO> spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;
}

