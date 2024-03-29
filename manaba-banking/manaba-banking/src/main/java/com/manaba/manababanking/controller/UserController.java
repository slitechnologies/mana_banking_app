package com.manaba.manababanking.controller;

import com.manaba.manababanking.dto.*;
import com.manaba.manababanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@SuppressWarnings(value = "all")
public class UserController {
    private final UserService userService;



    @PostMapping
    public BankResponse createdAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }
    @PostMapping("/login")
    public BankResponse login(LoginRequest request){
        return userService.login(request);
    }

    @GetMapping(value = "/balance-enquiry", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @PostMapping("/credit-account")
    public BankResponse  creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }
    @PostMapping("/debit-account")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}
