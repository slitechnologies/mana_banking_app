package com.manaba.manababanking.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "200";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User already has account Created!";
    public static final String ACCOUNT_CREATION_MESSAGE = "201";
    public static final String ACCOUNT_CREATION_SUCCESS = "Account Successfully Created";
    public static final String ACCOUNT_NOT_EXIST_CODE = "404";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account does not exist";
    public static final String ACCOUNT_FOUND_CODE = "200";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "201";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account Credited Successfully";

    public static final String INSUFFICIENT_BALANCE_CODE = "406";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "205";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account Debited Successfully";
    public static final String TRANSFER_SUCCESSFUL_CODE = "2006";
    public  static final String TRANSFER_SUCCESSFUL_MESSAGE = "Your transfer was successful";




    public static String generateAccountNumber() {
        /*
         * year + anyRandomSixDigits
         * */

        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        // generate a random number between min and max
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }
}
