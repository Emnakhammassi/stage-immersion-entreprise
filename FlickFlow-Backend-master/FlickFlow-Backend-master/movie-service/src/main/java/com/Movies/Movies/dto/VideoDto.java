// file: src/main/java/com/Movies/Movies/dto/VideoDto.java
package com.Movies.Movies.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private String name;
    private String key;
    private String site;
    private String type;
    private boolean official;
    private String id; // Video ID, not the movie ID
}
