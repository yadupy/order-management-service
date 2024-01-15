package com.accenture.pip.ordermanagementservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OIDCTokenConfig {

    @Value("${oidc.service.keycloak.token-endpoint-uri:http://0.0.0.0:8080/realms/pip-iap/protocol/openid-connect/}")
    private String tokenUri;

    @Value("${oidc.service.keycloak.client-id:pip-chuggy}")
    private String clientId;

    @Value("${oidc.service.keycloak.client-secret:0bVWGhvThUO8aN8WAfLj7WPHA3aysvRh}")
    private String clientSecret;


}
