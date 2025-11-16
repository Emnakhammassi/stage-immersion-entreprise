// file: src/main/java/com/Movies/movies/repository/MovieRepository.java
package com.Movies.Movies.repository;

import com.Movies.Movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface movieRepository extends JpaRepository<Movie, Integer> {
    Movie findByMovieName(String movieName);
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findByGenreId(@Param("genreId") Long genreId);


}
