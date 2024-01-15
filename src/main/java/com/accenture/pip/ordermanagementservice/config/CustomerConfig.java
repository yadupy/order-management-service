package com.accenture.pip.ordermanagementservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CustomerConfig {

    @Value("${customer.service.uri:http://localhost:9090/customer-ws/customerManagement}")
    private String serviceUri;


    @Value("${customer.service.keycloak.token-endpoint-uri}")
    private String tokenUri;

    @Value("${customer.service.keycloak.client-id}")
    private String clientId;

    @Value("${customer.service.keycloak.client-secret}")
    private String clientSecret;

    @Value("${customer.service.keycloak.userName}")
    private String userName;

    @Value("${customer.service.keycloak.password}")
    private String password;

}
