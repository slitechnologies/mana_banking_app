package com.manaba.manababanking.dto;

import com.manaba.manababanking.constants.TransactionStatus;
import com.manaba.manababanking.constants.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private TransactionType transactionType;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionStatus status;
}
