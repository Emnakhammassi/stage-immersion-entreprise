// file: src/main/java/com/Movies/Movies/dto/MovieDto.java
package com.Movies.Movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class movieDto {
    private int tmbdId;
    private String movieName;
    private List<Integer> genreIds;
    private float rating;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private String overview;
}
