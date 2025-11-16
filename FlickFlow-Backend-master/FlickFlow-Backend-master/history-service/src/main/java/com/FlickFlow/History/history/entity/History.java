package com.FlickFlow.History.history.entity;

import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.Movies.Movies.entity.Movie;
import com.FlickFlow.FlickFlow.user.entity.user;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history")
public class History {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private int historyId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "movie_id", nullable = false)
    private int movieId;

    @Column(name = "watched_at", nullable = false)
    private LocalDateTime watchedAt;
}
