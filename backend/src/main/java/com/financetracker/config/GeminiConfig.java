package com.financetracker.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a personal finance assistant. Analyze the user's transaction data " +
                        "and give clear, concise, actionable financial insights. Always respond in English.")
                .build();
    }
}
