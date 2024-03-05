package com.manaba.manababanking.service;

import com.manaba.manababanking.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
