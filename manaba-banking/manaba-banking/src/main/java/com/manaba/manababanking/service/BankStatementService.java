package com.manaba.manababanking.service;

import com.itextpdf.text.DocumentException;
import com.manaba.manababanking.entity.Transaction;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatementService {

    List<Transaction> generateBankStatement(String accountNumber, String startDate, String endDate ) throws FileNotFoundException, DocumentException;
}
