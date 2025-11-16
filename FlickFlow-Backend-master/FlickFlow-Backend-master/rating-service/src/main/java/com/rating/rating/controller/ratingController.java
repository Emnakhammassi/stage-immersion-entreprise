// file: src/main/java/com/Movies/Movies/controller/RatingController.java
package com.rating.rating.controller;

import com.rating.rating.dto.RatingDto;
import com.rating.rating.entity.Rating;
import com.rating.rating.service.ratingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movie/rating")
@RequiredArgsConstructor
public class ratingController {
    private final ratingService ratingService;

    @PostMapping
    public ResponseEntity<Void> addRating(@RequestBody RatingDto ratingDto) {
        ratingService.addRating(ratingDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<RatingDto>> getRatingsByMovieId(@PathVariable int movieId) {
        List<RatingDto> ratings = ratingService.getRatings(movieId);
        return ResponseEntity.ok(ratings);
    }
    @GetMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<RatingDto> getUserRating(@PathVariable int userId, @PathVariable int movieId) {
        Optional<RatingDto> rating = ratingService.findByUserIdAndMovieId(userId, movieId);
        return rating.map(value -> ResponseEntity.ok(new RatingDto(value.getUserId(), value.getMovieId(), value.getRatingValue())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
