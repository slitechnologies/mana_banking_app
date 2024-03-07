package com.manaba.manababanking.service.serviceImpl;

import com.manaba.manababanking.entity.Transaction;
import com.manaba.manababanking.repository.TransactionRepository;
import com.manaba.manababanking.service.BankStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {
    private final TransactionRepository transactionRepository;

    /*
    *  retrieve transactions within a date range given an account number
    * generate a pdf file of transactions
    * send the file via email
    *  */

    public List<Transaction> generateBankStatement(String accountNumber, String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
                .toList();
    }
}