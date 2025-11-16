package com.Movies.Movies.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBCast {

    @JsonProperty("id")
    private int id; // Movie ID

    @JsonProperty("cast")
    private List<CastMember> cast; // List of cast members

    @JsonProperty("crew")
    private List<CrewMember> crew; // List of crew members

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CastMember {
        @JsonProperty("adult")
        private boolean adult;

        @JsonProperty("gender")
        private int gender; // 1 for female, 2 for male, 0 for unknown

        @JsonProperty("id")
        private int id;

        @JsonProperty("known_for_department")
        private String knownForDepartment;

        @JsonProperty("name")
        private String name;

        @JsonProperty("original_name")
        private String originalName;

        @JsonProperty("popularity")
        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("cast_id")
        private int castId;

        @JsonProperty("character")
        private String character;

        @JsonProperty("credit_id")
        private String creditId;

        @JsonProperty("order")
        private int order;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CrewMember {
        @JsonProperty("adult")
        private boolean adult;

        @JsonProperty("gender")
        private int gender; // 1 for female, 2 for male, 0 for unknown

        @JsonProperty("id")
        private int id;

        @JsonProperty("known_for_department")
        private String knownForDepartment;

        @JsonProperty("name")
        private String name;

        @JsonProperty("original_name")
        private String originalName;

        @JsonProperty("popularity")
        private double popularity;

        @JsonProperty("profile_path")
        private String profilePath;

        @JsonProperty("credit_id")
        private String creditId;

        @JsonProperty("department")
        private String department;

        @JsonProperty("job")
        private String job;
    }
}
