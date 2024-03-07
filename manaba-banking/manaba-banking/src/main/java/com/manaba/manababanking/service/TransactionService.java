package com.manaba.manababanking.service;

import com.manaba.manababanking.dto.TransactionRequest;

public interface TransactionService {
    void saveTransaction(TransactionRequest transactionRequest);
}
