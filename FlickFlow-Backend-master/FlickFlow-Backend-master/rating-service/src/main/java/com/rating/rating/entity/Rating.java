// file: src/main/java/com/Movies/Movies/entity/Rating.java
package com.rating.rating.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private int ratingId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "movie_id")
    private int movieId;
    @Column(name = "rating_value")
    private float ratingValue;
}
