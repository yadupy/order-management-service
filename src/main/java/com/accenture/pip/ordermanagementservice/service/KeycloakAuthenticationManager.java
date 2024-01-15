package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.config.OIDCTokenConfig;
import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import com.accenture.pip.ordermanagementservice.dto.GrantType;
import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
import com.accenture.pip.ordermanagementservice.dto.customer.AddressDTO;
import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationRequest;

import com.accenture.pip.ordermanagementservice.dto.security.AuthenticationResponse;
import com.accenture.pip.ordermanagementservice.exception.AuthenticationException;
import com.accenture.pip.ordermanagementservice.util.CustomerUtil;
import com.accenture.pip.ordermanagementservice.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class KeycloakAuthenticationManager {

    @Autowired
    OIDCTokenConfig oidcTokenConfig;

    @Autowired
    @Qualifier("keycloakRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("keycloakWebClient")
    WebClient webClient;

    @Autowired
    CustomerConfig customerConfig;

    private String accessToken = "";

    private static final String INVALID_CUSTOMER = "INVALID";

    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        Optional<OIDCToken> oidcAccessToken = fetchOIDCAccessTokenWithReactive(request, oidcTokenConfig.getClientId(), oidcTokenConfig.getClientSecret());
        if (oidcAccessToken.isPresent()) {
            CustomerResponse customerResponse = getCustomerByUserName(request.getUserName());
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(oidcAccessToken.get().getAccessToken(),
                    oidcAccessToken.get().getRefreshToken(),
                    customerResponse);
            return Mono.just(authenticationResponse);
        } else {
            throw new AuthenticationException();
        }
    }

    private HttpHeaders creteHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> creteHeaderMap() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> createBody(AuthenticationRequest request, String clientId, String clientSecret) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GrantType.PASSWORD.grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", request.getUserName());
        body.add("password", request.getPassword());
        return body;
    }

    public String getAccessToken() {
        /*if (this.isFetchToken(accessToken)) {*/
        if(true){
            this.fetchGlobalAccessToken();
        }
        return this.accessToken;
    }

    private void fetchGlobalAccessToken() {
        AuthenticationRequest request = new AuthenticationRequest(
                customerConfig.getUserName(), customerConfig.getPassword());
        this.accessToken = fetchOIDCAccessToken(
                request, customerConfig.getClientId(), customerConfig.getClientSecret());

    }

    private String fetchOIDCAccessToken(AuthenticationRequest request, String clientId, String clientSecret) {
        HttpHeaders headers = creteHeaders();
        MultiValueMap<String, String> body = createBody(
                request, clientId, clientSecret);
        HttpEntity entity = new HttpEntity(body, headers);
        ResponseEntity<OIDCToken> response = restTemplate.exchange(
                oidcTokenConfig.getTokenUri() + "token", HttpMethod.POST, entity, OIDCToken.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            OIDCToken token = (OIDCToken) response.getBody();
            log.info("token retrieved: {}", token);
            return token.getAccessToken();
        } else {
            throw new AuthenticationException(new Throwable("Authentication failed with status code: " + response.getStatusCode()));
        }
    }

    public Optional<OIDCToken> fetchOIDCAccessTokenWithReactive(AuthenticationRequest request, String clientId, String clientSecret) {

        MultiValueMap<String, String> body = createBody(
                request, clientId, clientSecret);

        Optional<OIDCToken> oidcToken = webClient.post()
                .uri(oidcTokenConfig.getTokenUri() + "token")
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .body(BodyInserters.fromFormData(body))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(OIDCToken.class);
                    } else {
                        throw new AuthenticationException(new Throwable("Authentication failed with status code: " + response.statusCode()));
                    }
                })
                .onErrorMap(response -> {
                    throw new AuthenticationException(new Throwable("Authentication failed with status code: " + response.getMessage()));
                })
                .blockOptional();
        return oidcToken;
        /*OIDCToken oidcToken = webClient.post()
                .uri(oidcTokenConfig.getTokenUri() + "token")
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                //.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(AuthenticationException::new))
                .bodyToMono(OIDCToken.class)
                .blockOptional()
                .orElseThrow(()->new AuthenticationException());
        */


    }

    private boolean isFetchToken(String accessToken) {
        boolean isNeedToFetchToken = Objects.isNull(accessToken) || TokenUtil.isTokenValid(accessToken, Instant.now());
        return isNeedToFetchToken;
    }

    public Mono<String> logout(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("client_id", oidcTokenConfig.getClientId());
        body.add("client_secret", oidcTokenConfig.getClientSecret());
        return webClient.post()
                .uri(oidcTokenConfig.getTokenUri() + "logout")
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .body(BodyInserters.fromFormData(body))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just("successfully logged out from keycloak");
                    } else {
                        throw new AuthenticationException(new Throwable("logout failed with status code: " + response.statusCode()));
                    }
                })
                .onErrorMap(response -> {
                    throw new AuthenticationException(new Throwable("logout failed with status code: " + response.getMessage()));
                });
    }

    public CustomerResponse getCustomerByUserName(String userName) {

        log.info("calling customer service api to validate userName: {}", userName);
        try {
            String accessToken = getAccessToken();
            String baseUri = customerConfig.getServiceUri() + ApplicationConstant.CUSTOMER_SERVICE_API_BASE
                    + ApplicationConstant.CUSTOMER_GET_CUSTOMER_URI + "userName/" + userName;
            HttpHeaders headers = new HttpHeaders();
            headers.add(ApplicationConstant.AUTHORIZATION, ApplicationConstant.TOKEN_PREFIX + accessToken);
            HttpEntity entity = new HttpEntity(null, headers);
            ResponseEntity responseEntity = restTemplate.exchange(baseUri, HttpMethod.GET, entity, CustomerResponse.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("customer service responded with status: {}", responseEntity.getStatusCode());
                return (CustomerResponse) responseEntity.getBody();
            } else {
                log.error("customer service api call failed with error code: {}", responseEntity.getStatusCode());
                return createDefaultCustomer(userName);
            }
        }catch (RuntimeException exception){
            log.error("customer service api call failed with exception: {}", exception.getMessage());
            return createDefaultCustomer(userName);
        }
    }
    private CustomerResponse createDefaultCustomer(String userName){
        AddressDTO addressDTO = AddressDTO.builder()
                .addressId(INVALID_CUSTOMER)
                .city(INVALID_CUSTOMER)
                .state(INVALID_CUSTOMER)
                .pinCode(INVALID_CUSTOMER)
                .houseNo(INVALID_CUSTOMER)
                .street(INVALID_CUSTOMER)
                .build();
        return  CustomerResponse
                .builder()
                .id(INVALID_CUSTOMER)
                .firstName(INVALID_CUSTOMER)
                .lastName(INVALID_CUSTOMER)
                .email(INVALID_CUSTOMER)
                .contactNumber(INVALID_CUSTOMER)
                .userName(userName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .addressDTOs(Set.of(addressDTO))
                .build();

    }
}
