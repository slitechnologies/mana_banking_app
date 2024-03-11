package com.manaba.manababanking.controller;

import com.itextpdf.text.DocumentException;
import com.manaba.manababanking.entity.Transaction;
import com.manaba.manababanking.service.BankStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("bank-statement")
@RequiredArgsConstructor
@SuppressWarnings(value = "all")
public class TransactionController {
    private final BankStatementService bankStatementService;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateBankStatement(accountNumber, startDate, endDate);

    }
}
