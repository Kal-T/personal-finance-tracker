package com.financetracker.service;

import com.financetracker.exception.ResourceNotFoundException;
import com.financetracker.model.SummaryDTO;
import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionDTO;
import com.financetracker.model.TransactionMapper;
import com.financetracker.model.TransactionType;
import com.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return TransactionMapper.toDTO(transaction);
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction transaction = TransactionMapper.toEntity(dto);
        Transaction saved = transactionRepository.save(transaction);
        return TransactionMapper.toDTO(saved);
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO dto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        transaction.setAmount(dto.getAmount());
        transaction.setCategory(dto.getCategory());
        transaction.setType(dto.getType());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());

        Transaction updated = transactionRepository.save(transaction);
        return TransactionMapper.toDTO(updated);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    public SummaryDTO getSummary() {
        List<Transaction> transactions = transactionRepository.findAll();

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return SummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }
}
