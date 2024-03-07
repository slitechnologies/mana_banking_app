package com.manaba.manababanking.service;

import com.manaba.manababanking.entity.Transaction;

import java.util.List;

public interface BankStatementService {

    List<Transaction> generateBankStatement(String accountNumber, String startDate, String endDate );
}
