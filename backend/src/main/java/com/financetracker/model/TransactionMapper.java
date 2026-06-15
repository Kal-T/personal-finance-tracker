package com.financetracker.model;

public class TransactionMapper {

    public static TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        return Transaction.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .category(dto.getCategory())
                .type(dto.getType())
                .description(dto.getDescription())
                .date(dto.getDate())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
