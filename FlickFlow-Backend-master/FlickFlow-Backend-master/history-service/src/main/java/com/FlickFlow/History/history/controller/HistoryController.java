package com.FlickFlow.History.history.controller;

import com.FlickFlow.History.history.entity.History;
import com.FlickFlow.History.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<Void> savetoHistory(@PathVariable int userId, @PathVariable int movieId) {
        historyService.addHistory(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<History>> getUserHistory(@PathVariable int userId) {
        List<History> historyList = historyService.getUserHistory(userId);
        return ResponseEntity.ok(historyList);
    }
}