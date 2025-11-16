// file: src/main/java/com/Movies/Movies/repository/RatingRepository.java
package com.rating.rating.repository;

import  com.rating.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByMovieId(int movieId);
    Optional<Rating> findByUserIdAndMovieId(int userId, int movieId);
    List<Rating> findByUserId(int userId);

}
