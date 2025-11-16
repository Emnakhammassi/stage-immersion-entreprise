// file: src/main/java/com/Movies/Movies/dto/CastDto.java
package com.Movies.Movies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CastDto {
    private String name;
    private String character;
    private String profilePath;
    private boolean adult;
    private int gender;
    private int id;
    private String knownForDepartment;
    private String originalName;
    private double popularity;
    private int castId;
    private String creditId;
    private int order;

}

