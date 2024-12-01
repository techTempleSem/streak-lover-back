package com.example.streak.email.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailInquiryRequest {
    @NonNull
    private String title;
    @NonNull
    private String context;
}
