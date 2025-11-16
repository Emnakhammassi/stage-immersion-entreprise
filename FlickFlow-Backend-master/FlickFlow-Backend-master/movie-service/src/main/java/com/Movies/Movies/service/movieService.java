// file: src/main/java/com/Movies/Movies/service/movieService.java
package com.Movies.Movies.service;

import com.Movies.Movies.clients.CastResponse;
import com.Movies.Movies.clients.VidSrc;
import com.Movies.Movies.dto.CastDto;
import com.Movies.Movies.dto.MovieDetailsDTO;
import com.Movies.Movies.dto.VideoDto;
import com.Movies.Movies.dto.movieDto;
import com.Movies.Movies.entity.Genres;
import com.Movies.Movies.entity.Movie;
import com.Movies.Movies.repository.movieRepository;
import com.Movies.Movies.clients.TMDBClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class movieService {
    private final movieRepository movieRepository;
    private final TMDBClient tmdbClient;
    private final VidSrc vidSrc;

    public movieDto findMovieByName(String title) {
        System.out.println("Searching for movie: " + title); // Debugging line
        Movie movie = movieRepository.findByMovieName(title);
        if (movie == null) {
            movie = tmdbClient.fetchMovieByTitle(title);

        }
        return convertToDto(movie);
    }

    public List<movieDto> findTrendingMovies() {
        List<Movie> movie = tmdbClient.fetchTrendingMovies();
        return movie.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<movieDto> findNowPlayingMovies() {
        return tmdbClient.fetchNowPlayingMovies();
    }

    public List<movieDto> findMoviesByGenre(List<Integer> genreIds) {
        List<Movie> moviesByGenre = tmdbClient.fetchMoviesByGenre(genreIds);
        return moviesByGenre.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public MovieDetailsDTO fetchMovieDetails(int movieId) {
        // Convert MovieDetailsDTO to Movie entity if necessary or handle directly as DTO
        return tmdbClient.fetchMovieDetails(movieId);
    }


    public List<CastDto> fetchMovieCast(int movieId) {
        return tmdbClient.fetchMovieCast(movieId);
    }



    // Fetch list of video details for a movie
    public List<VideoDto> fetchMovieVideos(int movieId) {
        return tmdbClient.getMovieVideos(String.valueOf(movieId));
    }

    public List<movieDto> getHomePageMovies() {
        List<Movie> movies = tmdbClient.fetchPopularMovies();
        return movies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Optional<String> streamMovie(int movieId) {
        return vidSrc.streamMovie(String.valueOf(movieId));
    }

    public Optional<String> streamTvShow(int TvShowId, int saison, int episode) {
        return vidSrc.streamTVShow(String.valueOf(TvShowId), saison, episode);
    }

    private movieDto convertToDto(Movie movie) {
        if (movie == null) return null;
        List<Integer> genreIds = movie.getGenres().stream()
                .map(Genres::getId)
                .collect(Collectors.toList());
        return new movieDto(
                movie.getTmbdId(),
                movie.getMovieName(),
                genreIds,
                movie.getRating(),
                movie.getReleaseDate(),
                movie.getPosterPath(),
                movie.getBackdropPath(),
                null
        );
    }


    public Movie getMovieById(Integer movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }


    public List<MovieDetailsDTO> getRecommendedMovieDetails(List<Integer> movieIds) {
        return movieIds.stream()
                .map(this::fetchMovieDetails)
                .collect(Collectors.toList());
    }

}
