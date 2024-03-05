package com.manaba.manababanking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryRequest {
    private String accountNumber;
}
