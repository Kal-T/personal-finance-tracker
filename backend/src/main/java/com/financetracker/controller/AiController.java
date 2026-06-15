package com.financetracker.controller;

import com.financetracker.agent.AnalyzerAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final ChatClient chatClient;
    private final AnalyzerAgent analyzerAgent;

    @Value("${spring.ai.google.gemini.api-key:}")
    private String apiKey;

    public AiController(ChatClient chatClient, AnalyzerAgent analyzerAgent) {
        this.chatClient = chatClient;
        this.analyzerAgent = analyzerAgent;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        if (apiKey == null || apiKey.trim().isEmpty() || "${GEMINI_API_KEY}".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("API Key is not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        try {
            String response = chatClient.prompt("Say hello and confirm you are ready.")
                    .call()
                    .content();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI service call failed: " + e.getMessage());
        }
    }

    @GetMapping("/analyze")
    public ResponseEntity<String> analyze(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (apiKey == null || apiKey.trim().isEmpty() || "${GEMINI_API_KEY}".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("API Key is not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        try {
            String report = analyzerAgent.analyze(from, to);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI analysis call failed: " + e.getMessage());
        }
    }
}

