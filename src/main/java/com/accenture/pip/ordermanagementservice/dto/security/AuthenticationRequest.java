package com.accenture.pip.ordermanagementservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AuthenticationRequest {

    private String userName;
    private String password;

}
