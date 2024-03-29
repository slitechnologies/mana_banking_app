package com.manaba.manababanking.service.serviceImpl;

import com.manaba.manababanking.config.JwtTokenProvider;
import com.manaba.manababanking.constants.ObjectStatus;
import com.manaba.manababanking.constants.Role;
import com.manaba.manababanking.constants.TransactionType;
import com.manaba.manababanking.dto.*;
import com.manaba.manababanking.entity.User;
import com.manaba.manababanking.repository.UserRepository;
import com.manaba.manababanking.service.EmailService;
import com.manaba.manababanking.service.TransactionService;
import com.manaba.manababanking.service.UserService;
import com.manaba.manababanking.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*
         *checking if user already has an account
         */
        /*
         * Creating an account-saving new user into the db
         * */

        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        var user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .role(Role.USER)
                .status(ObjectStatus.ACTIVE)
                .build();
        var savedUser = userRepository.save(user);

        //send and email alert to the user
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your account has been Successfully Created\nYour Account Details:\n Account Name: "+savedUser.getFirstName()+
                        " "+savedUser.getOtherName()+" "+savedUser.getLastName()+"\n Account Number: "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstName() + " "
                                + savedUser.getOtherName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse login(LoginRequest loginRequest) {
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),loginRequest.getPassword()));
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("Login Reporting ")
                .recipient(loginRequest.getEmail())
                .messageBody("Someone have logged in to your account. " +
                        "If you did not initiate this login please contact your bank!")
                .build();
        emailService.sendEmailAlert(loginAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS)
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        /*
         *checking if provided user account exists in the db
         */

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())

                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        //save credit Transaction
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType(TransactionType.CREDIT)
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getOtherName() + " "
                                + userToCredit.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            //save  debit Transaction
            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .transactionType(TransactionType.DEBIT)
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(request.getAmount())
                    .build();
            transactionService.saveTransaction(transactionRequest);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " +
                                    userToDebit.getOtherName() + " "
                                    + userToDebit.getLastName())
                            .accountNumber(request.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        String sourceUsername = sourceAccountUser.getFirstName()+" "+sourceAccountUser.getOtherName()+ " "+sourceAccountUser.getLastName();
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);
        //send and email alert to the user
        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(sourceAccountUser.getEmail())
                .subject("DEBIT TRANSFER ALERT")
                .messageBody("Hie "+sourceUsername+" A Sum of "+request.getAmount()+" has been deducted from your account ending 2******"+sourceAccountUser.getAccountNumber().substring(7, 10) +" Your current balance is "+sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        //save  debit Transfer Transaction
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType(TransactionType.DEBIT)
                .accountNumber(sourceAccountUser.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionRequest);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
       String recipientUsername = destinationAccountUser.getFirstName()+" "+destinationAccountUser.getOtherName()+" "+destinationAccountUser.getLastName();
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(destinationAccountUser.getEmail())
                .subject("CREDIT TRANSFER ALERT")
                .messageBody("Hie "+recipientUsername+" A Sum of "+request.getAmount()+" has been sent to your account ending 2******"+destinationAccountUser.getAccountNumber().substring(7, 10)+" ! Your current balance is "+destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        //save  credit Transfer Transaction
        TransactionRequest transaction = TransactionRequest.builder()
                .transactionType(TransactionType.CREDIT)
                .accountNumber(destinationAccountUser.getAccountNumber())
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transaction);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(recipientUsername)
                        .accountNumber(destinationAccountUser.getAccountNumber())
                        .accountBalance(destinationAccountUser.getAccountBalance())
                        .build())
                .build();
    }
}