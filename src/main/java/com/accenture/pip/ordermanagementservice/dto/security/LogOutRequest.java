package com.accenture.pip.ordermanagementservice.dto.security;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LogOutRequest {

    @NotNull(message = "refresh token can not be null")
    private String refreshToken;
}
