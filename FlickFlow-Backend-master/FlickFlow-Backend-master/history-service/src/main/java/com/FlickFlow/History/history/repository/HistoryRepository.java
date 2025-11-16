package com.FlickFlow.History.history.repository;

import com.FlickFlow.FlickFlow.user.entity.user;
import com.FlickFlow.History.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    @Query("SELECT h FROM History h WHERE h.userId = :userId AND h.movieId = :movieId")
    History findByUserIdAndMovieId(@Param("userId") int userId, @Param("movieId") int movieId);

    List<History> findByUserId(int userId);
}
