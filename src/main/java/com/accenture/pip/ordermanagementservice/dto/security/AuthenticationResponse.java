package com.accenture.pip.ordermanagementservice.dto.security;

import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AuthenticationResponse {

    private final String accessToken;

    private final String refreshToken;

    private final CustomerResponse customerResponse;
}
