package com.manaba.manababanking.service.serviceImpl;

import com.manaba.manababanking.constants.TransactionStatus;
import com.manaba.manababanking.dto.TransactionRequest;
import com.manaba.manababanking.entity.Transaction;
import com.manaba.manababanking.repository.TransactionRepository;
import com.manaba.manababanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .status(TransactionStatus.SUCCESS)
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");

    }
}
