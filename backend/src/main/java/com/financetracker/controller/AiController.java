package com.financetracker.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final ChatClient chatClient;

    @Value("${spring.ai.google.gemini.api-key:}")
    private String apiKey;

    public AiController(ChatClient chatClient) {
        this.chatClient = chatClient;
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
}
