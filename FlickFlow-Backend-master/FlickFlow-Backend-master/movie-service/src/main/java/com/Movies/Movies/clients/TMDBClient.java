// file: src/main/java/com/Movies/Movies/clients/TMDBClient.java
package com.Movies.Movies.clients;

import com.Movies.Movies.dto.*;
import com.Movies.Movies.entity.Movie;
import com.Movies.Movies.entity.Genres;
import com.Movies.Movies.clients.Genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TMDBClient {

    private final String apikey;
    private final HttpClient httpClient;
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    @Autowired
    public TMDBClient(@Value("${tmdb.api.key}") String apikey, HttpClient httpClient) {
        this.apikey = apikey;
        this.httpClient = httpClient;
    }

    // Fetch movie details by title
    public Movie fetchMovieByTitle(String title) {
        try {
            // URL encode the title to handle special characters
            String encodedTitle = java.net.URLEncoder.encode(title, "UTF-8");
            String url = String.format("%s/search/movie?api_key=%s&query=%s", BASE_URL, apikey, encodedTitle);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + apikey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("TMDB Response: " + response.body()); // Debugging line

            TMDBMovieResponse tmdbResponse = parseMovieResponseBody(response.body());

            if (tmdbResponse != null && tmdbResponse.getMovieResults() != null && !tmdbResponse.getMovieResults().isEmpty()) {
                return mapToMovie(tmdbResponse.getMovieResults().get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch movie details by ID
    public MovieDetailsDTO fetchMovieDetails(int movieId) {
        try {
            String url = String.format("%s/movie/%d?api_key=%s", BASE_URL, movieId, apikey);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + apikey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("TMDB Response: " + response.body()); // Debugging line

            MovieDetails movieDetails = parseMovieDetailsResponseBody(response.body());

            if (movieDetails != null) {
                return mapToMovieDetailsDTO(movieDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<VideoDto> getMovieVideos(String movieId) {
        try {
            String url = String.format("%s/movie/%s/videos?api_key=%s", BASE_URL, movieId, apikey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + apikey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("TMDB Response: " + response.body()); // Debugging line

            TMDBVideoResponse tmdbResponse = parseVideoResponseBody(response.body());

            if (tmdbResponse != null && tmdbResponse.getVideoResults() != null && !tmdbResponse.getVideoResults().isEmpty()) {
                return tmdbResponse.getVideoResults().stream()
                        .filter(video -> "Clip".equalsIgnoreCase(video.getType())) // Filter only "Clip" type videos
                        .map(this::mapToVideoDto)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }


    public List<CastDto> fetchMovieCast(int movieId) {
        try {
            String url = String.format("%s/movie/%d/credits?api_key=%s", BASE_URL, movieId, apikey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + apikey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("TMDB Response: " + response.body());

            CastResponse castResponse = parseCastResponseBody(response.body());

            if (castResponse != null && castResponse.getCast() != null) {
                return castResponse.getCast().stream()
                        .map(this::mapToCastDto)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }




    // Fetch popular movies
    public List<Movie> fetchPopularMovies() {
        String url = String.format("%s/movie/popular?api_key=%s", BASE_URL, apikey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        TMDBMovieResponse tmdbResponse = parseMovieResponseBody(response.body());

        return tmdbResponse != null && tmdbResponse.getMovieResults() != null
                ? tmdbResponse.getMovieResults().stream().map(this::mapToMovie).collect(Collectors.toList())
                : List.of();
    }

    // Fetch movies by genre
    public List<Movie> fetchMoviesByGenre(List<Integer> genreIds) {
        // Convert the list of genre IDs to a comma-separated string
        String genreIdsString = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String url = String.format("%s/discover/movie?api_key=%s&with_genres=%s", BASE_URL, apikey, genreIdsString);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        TMDBMovieResponse tmdbResponse = parseMovieResponseBody(response.body());

        return tmdbResponse != null && tmdbResponse.getMovieResults() != null
                ? tmdbResponse.getMovieResults().stream().map(this::mapToMovie).collect(Collectors.toList())
                : List.of();
    }


    // Fetch now playing movies
    public List<movieDto> fetchNowPlayingMovies() {
        String url = String.format("%s/movie/now_playing?api_key=%s", BASE_URL, apikey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        TMDBMovieResponse tmdbResponse = parseMovieResponseBody(response.body());

        return tmdbResponse != null && tmdbResponse.getMovieResults() != null
                ? tmdbResponse.getMovieResults().stream().map(this::mapToMovieDto).collect(Collectors.toList())
                : List.of();
    }

    // Fetch trending movies
    public List<Movie> fetchTrendingMovies() {
        String url = String.format("%s/trending/movie/week?api_key=%s", BASE_URL, apikey);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        TMDBMovieResponse tmdbResponse = parseMovieResponseBody(response.body());

        return tmdbResponse != null && tmdbResponse.getMovieResults() != null
                ? tmdbResponse.getMovieResults().stream().map(this::mapToMovie).collect(Collectors.toList())
                : List.of();
    }

    // Mapping from TMDBMovie to Movie
    private Movie mapToMovie(TMDBMovie tmdbMovie) {
        Movie movie = new Movie();
        movie.setTmbdId(tmdbMovie.getId());
        movie.setMovieName(tmdbMovie.getTitle());
        movie.setRating((float) tmdbMovie.getVoteAverage());
        movie.setReleaseDate(tmdbMovie.getReleaseDate());
        movie.setPosterPath(tmdbMovie.getPosterPath());
        movie.setBackdropPath(tmdbMovie.getBackdropPath());
        // Map genres from TMDB response to entity
        Set<Genres> genres = tmdbMovie.getGenreIds().stream()
                .map(genreId -> {
                    Genres genre = new Genres();
                    genre.setId(genreId);
                    genre.setName(""); // Optionally set the name if needed
                    return genre;
                })
                .collect(Collectors.toSet());
        movie.setGenres(genres);
        return movie;
    }
    private movieDto mapToMovieDto(TMDBMovie tmdbMovie) {
        movieDto movie = new movieDto();
        movie.setTmbdId(tmdbMovie.getId());
        movie.setMovieName(tmdbMovie.getTitle());
        movie.setRating((float) tmdbMovie.getVoteAverage());
        movie.setReleaseDate(tmdbMovie.getReleaseDate());
        movie.setOverview(tmdbMovie.getOverview());
        movie.setPosterPath(tmdbMovie.getPosterPath());
        movie.setBackdropPath(tmdbMovie.getBackdropPath());

        // Map genre IDs directly to the DTO
        List<Integer> genreIds = tmdbMovie.getGenreIds();
        movie.setGenreIds(genreIds);

        return movie;
    }


    // Mapping from TMDBVideo to VideoDto
    private VideoDto mapToVideoDto(TMDBVideo tmdbVideo) {
        return VideoDto.builder()
                .name(tmdbVideo.getName())
                .key(tmdbVideo.getKey())
                .site(tmdbVideo.getSite())
                .type(tmdbVideo.getType())
                .official(tmdbVideo.isOfficial())
                .id(tmdbVideo.getId())
                .build();
    }

    // Parsing movie response
    private TMDBMovieResponse parseMovieResponseBody(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, TMDBMovieResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Parsing video response
    private TMDBVideoResponse parseVideoResponseBody(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, TMDBVideoResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private CastResponse parseCastResponseBody(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, CastResponse.class);
    }
    private MovieDetails parseMovieDetailsResponseBody(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, MovieDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private CastDto mapToCastDto(TMDBCast.CastMember tmdbCast) {
        return new CastDto(
                tmdbCast.getName(),
                tmdbCast.getCharacter(),
                tmdbCast.getProfilePath(),
                tmdbCast.isAdult(),
                tmdbCast.getGender(),
                tmdbCast.getId(),
                tmdbCast.getKnownForDepartment(),
                tmdbCast.getOriginalName(),
                tmdbCast.getPopularity(),
                tmdbCast.getCastId(),
                tmdbCast.getCreditId(),
                tmdbCast.getOrder()
        );
    }
    private MovieDetailsDTO mapToMovieDetailsDTO(MovieDetails movieDetails) {
        if (movieDetails == null) return null;
        MovieDetailsDTO dto = new MovieDetailsDTO();
        dto.setAdult(movieDetails.isAdult());
        dto.setBackdropPath(movieDetails.getBackdropPath());
        dto.setBelongsToCollection(mapToCollectionDetailsDTO(movieDetails.getBelongsToCollection()));
        dto.setBudget(movieDetails.getBudget());
        dto.setGenres(movieDetails.getGenres().stream().map(this::mapToGenreDTO).collect(Collectors.toList()));
        dto.setHomepage(movieDetails.getHomepage());
        dto.setId(movieDetails.getId());
        dto.setImdbId(movieDetails.getImdbId());
        dto.setOriginalLanguage(movieDetails.getOriginalLanguage());
        dto.setOriginalTitle(movieDetails.getOriginalTitle());
        dto.setOverview(movieDetails.getOverview());
        dto.setPopularity(movieDetails.getPopularity());
        dto.setPosterPath(movieDetails.getPosterPath());
        dto.setProductionCompanies(movieDetails.getProductionCompanies().stream().map(this::mapToProductionCompanyDTO).collect(Collectors.toList()));
        dto.setProductionCountries(movieDetails.getProductionCountries().stream().map(this::mapToProductionCountryDTO).collect(Collectors.toList()));
        dto.setReleaseDate(movieDetails.getReleaseDate());
        dto.setRevenue(movieDetails.getRevenue());
        dto.setRuntime(movieDetails.getRuntime());
        dto.setSpokenLanguages(movieDetails.getSpokenLanguages().stream().map(this::mapToSpokenLanguageDTO).collect(Collectors.toList()));
        dto.setStatus(movieDetails.getStatus());
        dto.setTagline(movieDetails.getTagline());
        dto.setTitle(movieDetails.getTitle());
        dto.setVideo(movieDetails.isVideo());
        dto.setVoteAverage(movieDetails.getVoteAverage());
        dto.setVoteCount(movieDetails.getVoteCount());
        return dto;
    }
    private CollectionDetailsDTO mapToCollectionDetailsDTO(CollectionDetails collectionDetails) {
        if (collectionDetails == null) {
            return null;
        }
        CollectionDetailsDTO dto = new CollectionDetailsDTO();
        dto.setId(collectionDetails.getId());
        dto.setName(collectionDetails.getName());
        dto.setPosterPath(collectionDetails.getPosterPath());
        dto.setBackdropPath(collectionDetails.getBackdropPath());
        return dto;
    }

    private GenreDTO mapToGenreDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    private ProductionCompanyDTO mapToProductionCompanyDTO(ProductionCompany productionCompany) {
        if (productionCompany == null) {
            return null;
        }
        ProductionCompanyDTO dto = new ProductionCompanyDTO();
        dto.setId(productionCompany.getId());
        dto.setLogoPath(productionCompany.getLogoPath());
        dto.setName(productionCompany.getName());
        dto.setOriginCountry(productionCompany.getOriginCountry());
        return dto;
    }

    private ProductionCountryDTO mapToProductionCountryDTO(ProductionCountry productionCountry) {
        if (productionCountry == null) {
            return null;
        }
        ProductionCountryDTO dto = new ProductionCountryDTO();
        dto.setIso3166_1(productionCountry.getIso3166_1());
        dto.setName(productionCountry.getName());
        return dto;
    }

    private SpokenLanguageDTO mapToSpokenLanguageDTO(SpokenLanguage spokenLanguage) {
        if (spokenLanguage == null) {
            return null;
        }
        SpokenLanguageDTO dto = new SpokenLanguageDTO();
        dto.setEnglishName(spokenLanguage.getEnglishName());
        dto.setIso639_1(spokenLanguage.getIso639_1());
        dto.setName(spokenLanguage.getName());
        return dto;
    }

}
