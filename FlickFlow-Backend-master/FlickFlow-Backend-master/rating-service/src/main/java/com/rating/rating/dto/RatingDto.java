// file: src/main/java/com/Movies/Movies/dto/RatingDto.java
package com.rating.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private int movieId;
    private int userId;
    private float ratingValue;
}
