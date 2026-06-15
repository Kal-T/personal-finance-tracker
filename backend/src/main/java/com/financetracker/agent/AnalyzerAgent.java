package com.financetracker.agent;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnalyzerAgent {

      private final TransactionRepository transactionRepository;
      private final ChatClient chatClient;

      public String analyze(LocalDate from, LocalDate to) {
          List<Transaction> transactions = transactionRepository.findByDateBetween(from, to);

          BigDecimal totalIncome = BigDecimal.ZERO;
          BigDecimal totalExpenses = BigDecimal.ZERO;
          Map<String, BigDecimal> categoryExpenses = new HashMap<>();
          Map<String, BigDecimal> categoryIncome = new HashMap<>();

          for (Transaction t : transactions) {
              if (t.getType() == TransactionType.INCOME) {
                  totalIncome = totalIncome.add(t.getAmount());
                  categoryIncome.put(t.getCategory(), categoryIncome.getOrDefault(t.getCategory(), BigDecimal.ZERO).add(t.getAmount()));
              } else {
                  totalExpenses = totalExpenses.add(t.getAmount());
                  categoryExpenses.put(t.getCategory(), categoryExpenses.getOrDefault(t.getCategory(), BigDecimal.ZERO).add(t.getAmount()));
              }
          }
          BigDecimal balance = totalIncome.subtract(totalExpenses);

          StringBuilder sb = new StringBuilder();
          sb.append("Period: ").append(from).append(" to ").append(to).append("\n");
          sb.append("Total Income: $").append(totalIncome).append("\n");
          sb.append("Total Expenses: $").append(totalExpenses).append("\n");
          sb.append("Balance: $").append(balance).append("\n");
          sb.append("Transactions by category:\n");
          for (Map.Entry<String, BigDecimal> entry : categoryIncome.entrySet()) {
              sb.append("- ").append(entry.getKey()).append(": $").append(entry.getValue()).append(" (income)\n");
          }
          for (Map.Entry<String, BigDecimal> entry : categoryExpenses.entrySet()) {
              sb.append("- ").append(entry.getKey()).append(": $").append(entry.getValue()).append(" (expense)\n");
          }
          String summary = sb.toString();

          String prompt = String.format(
                  "Here is the user's financial data for the given period:\n%s\n\n" +
                  "Please analyze this data and provide:\n" +
                  "1. A brief overall assessment (2-3 sentences)\n" +
                  "2. Top 3 spending categories and whether they seem reasonable\n" +
                  "3. One concrete money-saving suggestion\n" +
                  "4. A savings rate calculation and what it means",
                  summary
          );

          return chatClient.prompt()
                  .user(prompt)
                  .call()
                  .content();
      }
  }
