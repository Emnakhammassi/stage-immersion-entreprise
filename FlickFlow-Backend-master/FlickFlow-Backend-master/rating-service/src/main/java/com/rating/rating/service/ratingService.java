// file: src/main/java/com/rating/rating/service/RatingService.java
package com.rating.rating.service;

import com.rating.rating.dto.RatingDto;
import com.rating.rating.entity.Rating;
import com.rating.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ratingService {  // Ensure the class name starts with an uppercase
    private final RatingRepository ratingRepository;

    public void addRating(RatingDto ratingDto) {
        Optional<Rating> existingRating = ratingRepository.findByUserIdAndMovieId(ratingDto.getUserId(), ratingDto.getMovieId());

        if (existingRating.isPresent()) {
            // Update the existing rating
            Rating rating = existingRating.get();
            rating.setRatingValue(ratingDto.getRatingValue());
            ratingRepository.save(rating);
        } else {
            // Create a new rating
            Rating rating = Rating.builder()
                    .movieId(ratingDto.getMovieId())
                    .userId(ratingDto.getUserId())
                    .ratingValue(ratingDto.getRatingValue())
                    .build();
            ratingRepository.save(rating);
        }
    }

    public List<RatingDto> getRatings(int movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);
        return ratings.stream()
                .map(rating -> new RatingDto(
                        rating.getUserId(),
                        rating.getMovieId(),
                        rating.getRatingValue()))
                .collect(Collectors.toList());
    }

    public Optional<RatingDto> findByUserIdAndMovieId(int userId, int movieId) {
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        return rating.map(r -> new RatingDto(
                r.getUserId(),
                r.getMovieId(),
                r.getRatingValue()));
    }


}
