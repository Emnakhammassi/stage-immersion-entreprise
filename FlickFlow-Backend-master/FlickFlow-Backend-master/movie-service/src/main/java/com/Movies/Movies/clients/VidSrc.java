package com.Movies.Movies.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class VidSrc {
    private final HttpClient httpClient;
    private static final String BASE_URL = "https://vidsrc.xyz/embed/";

    @Autowired
    public VidSrc(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public Optional<String> streamMovie(String movieId) {
        String movieUrl = BASE_URL + "movie/" + movieId;
        return fetchContent(movieUrl);
    }

    public Optional<String> streamTVShow(String showId, int season, int episode) {
        String tvShowUrl = BASE_URL + "tv/" + showId + "/" + season + "-" + episode;
        return fetchContent(tvShowUrl);
    }


    private Optional<String> fetchContent(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            } else {
                System.err.println("Failed to fetch content, status code: " + response.statusCode());
                return Optional.empty();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error occurred while fetching content: " + e.getMessage());
            return Optional.empty();
        }
    }
}
