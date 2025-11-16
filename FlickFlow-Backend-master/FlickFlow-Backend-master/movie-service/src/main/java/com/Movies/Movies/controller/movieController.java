// file: src/main/java/com/Movies/Movies/controller/movieController.java
package com.Movies.Movies.controller;

import com.Movies.Movies.dto.CastDto;
import com.Movies.Movies.dto.MovieDetailsDTO;
import com.Movies.Movies.dto.VideoDto;
import com.Movies.Movies.dto.movieDto;
import com.Movies.Movies.service.movieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class movieController {
    private final movieService movieService;

    @GetMapping("/{movieName}")
    public ResponseEntity<movieDto> findMovie(@PathVariable("movieName") String movieName) {
        System.out.println("Received request for movie: " + movieName); // Debugging line
        movieDto movie = movieService.findMovieByName(movieName);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieDetailsDTO> findMovieDetails(@PathVariable("movieId") int movieId) {
        System.out.println("Received request for movie: " + movieId); // Debugging line
        MovieDetailsDTO movie = movieService.fetchMovieDetails(movieId);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/cast/{movieId}")
    public ResponseEntity<List<CastDto>> findMovieCast(@PathVariable("movieId") int movieId) {
        List<CastDto> movies = movieService.fetchMovieCast(movieId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<movieDto>> findMoviesByGenre(@RequestParam("ids") List<Integer> genreIds) {
        List<movieDto> movies = movieService.findMoviesByGenre(genreIds);
        return ResponseEntity.ok(movies);
    }


    @GetMapping("/nowplaying")
    public ResponseEntity<List<movieDto>> findMoviesNowPlaying() {
        List<movieDto> movies = movieService.findNowPlayingMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<movieDto>> findMoviesTrending() {
        List<movieDto> movies = movieService.findTrendingMovies();
        return ResponseEntity.ok(movies);
    }

    // Return list of video data for the movie
    @GetMapping("/movievideo/{movieId}")
    public ResponseEntity<List<VideoDto>> getMovieVideos(@PathVariable("movieId") int movieId) {
        List<VideoDto> videos = movieService.fetchMovieVideos(movieId);
        if (!videos.isEmpty()) {
            return ResponseEntity.ok(videos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/home")
    public ResponseEntity<List<movieDto>> home() {
        // Fetch recommended or popular movies
        List<movieDto> movies = movieService.getHomePageMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/stream/movie/{movieId}")
    public ResponseEntity<String> streamMovie(@PathVariable("movieId") int movieId) {
        System.out.println("Received request to stream movie: " + movieId); // Debugging line
        Optional<String> movieContent = movieService.streamMovie(movieId);
        return movieContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stream/tvshow/{tvshowId}/{saison}/{episode}")
    public ResponseEntity<String> streamTvShow(@PathVariable("tvshowId") int tvshowId,
                                               @PathVariable("saison") int saison,
                                               @PathVariable("episode") int episode) {
        System.out.println("Received request to stream movie: " + tvshowId); // Debugging line
        Optional<String> movieContent = movieService.streamTvShow(tvshowId, saison, episode);
        return movieContent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
