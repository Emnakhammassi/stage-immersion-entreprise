package com.FlickFlow.History.history.service;

import com.FlickFlow.FlickFlow.user.dto.userDto;
import com.FlickFlow.FlickFlow.user.entity.user;
import com.FlickFlow.History.history.entity.History;
import com.FlickFlow.History.history.repository.HistoryRepository;
import com.Movies.Movies.entity.Movie;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import com.FlickFlow.FlickFlow.user.repository.userRepository ;
import com.Movies.Movies.repository.movieRepository;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public void addHistory(int userId, int movieId) {
        History existingHistory = historyRepository.findByUserIdAndMovieId(userId, movieId);
        if (existingHistory == null) {
            History history = History.builder()
                    .userId(userId)
                    .movieId(movieId)
                    .watchedAt(LocalDateTime.now())
                    .build();
            historyRepository.save(history);
        } else {
            // Handle the case where the history entry already exists (optional)
            // For example, you might want to update the watchedAt timestamp instead of creating a new entry.
            existingHistory.setWatchedAt(LocalDateTime.now());
            historyRepository.save(existingHistory);
        }
    }

    public List<History> getUserHistory(int userId) {
        return historyRepository.findByUserId(userId);
    }
}
