// file: src/main/java/com/Movies/Movies/clients/TMDBVideo.java
package com.Movies.Movies.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBVideo {
    @JsonProperty("iso_639_1")
    private String iso6391;

    @JsonProperty("iso_3166_1")
    private String iso31661;

    @JsonProperty("name")
    private String name;

    @JsonProperty("key")
    private String key;

    @JsonProperty("site")
    private String site;

    @JsonProperty("size")
    private int size;

    @JsonProperty("type")
    private String type;

    @JsonProperty("official")
    private boolean official;

    @JsonProperty("id")
    private String id;
}
