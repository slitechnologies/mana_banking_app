package com.manaba.manababanking.service;

import com.manaba.manababanking.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse login(LoginRequest loginRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);

    BankResponse transfer(TransferRequest request);
}
