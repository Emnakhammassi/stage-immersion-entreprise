// file: src/main/java/com/Movies/Movies/clients/TMDBMovieResponse.java
package com.Movies.Movies.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBMovieResponse {

    @JsonProperty("results")
    private List<TMDBMovie> movieResults;
}
