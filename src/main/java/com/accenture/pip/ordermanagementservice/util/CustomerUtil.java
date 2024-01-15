package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import com.accenture.pip.ordermanagementservice.dto.OIDCToken;
import com.accenture.pip.ordermanagementservice.dto.customer.AddressDTO;
import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import com.accenture.pip.ordermanagementservice.exception.AddressNotFoundException;
import com.accenture.pip.ordermanagementservice.exception.AuthenticationException;
import com.accenture.pip.ordermanagementservice.exception.CustomerNotFoundException;
import com.accenture.pip.ordermanagementservice.service.KeycloakAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CustomerUtil {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("keycloakWebClient")
    WebClient webClient;

    @Autowired
    CustomerConfig customerConfig;

    @Autowired
    KeycloakAuthenticationManager authenticationManager;

    //public static final String CMS_SERVLET_PATH = "/customerManagement";

    public CustomerResponse validateCustomerId(String customerId) {
        log.info("calling customer service api to validate customerId: {}", customerId);
        String accessToken = this.authenticationManager.getAccessToken();
        String baseUri = customerConfig.getServiceUri() + ApplicationConstant.CUSTOMER_SERVICE_API_BASE
                + ApplicationConstant.CUSTOMER_GET_CUSTOMER_URI + customerId;
        HttpHeaders headers = new HttpHeaders();
        headers.add(ApplicationConstant.AUTHORIZATION, ApplicationConstant.TOKEN_PREFIX + accessToken);
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity responseEntity = restTemplate.exchange(baseUri, HttpMethod.GET, entity, CustomerResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("customer service responded with status: {}", responseEntity.getStatusCode());
            return (CustomerResponse) responseEntity.getBody();
        } else {
            log.error("customer service api call failed with error code: {}", responseEntity.getStatusCode());
            throw new CustomerNotFoundException();
        }
    }

    public AddressDTO validateAddressId(String addressId) {
        log.info("calling customer service api to validate addressId: {}", addressId);
        String accessToken = this.authenticationManager.getAccessToken();
        String baseUri = customerConfig.getServiceUri() + ApplicationConstant.ADDRESS_SERVICE_API_BASE
                + ApplicationConstant.CUSTOMER_GET_CUSTOMER_URI + addressId;
        HttpHeaders headers = new HttpHeaders();
        headers.add(ApplicationConstant.AUTHORIZATION, ApplicationConstant.TOKEN_PREFIX + accessToken);
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity responseEntity = restTemplate.exchange(baseUri, HttpMethod.GET, entity, AddressDTO.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("customer service responded with status: {}", responseEntity.getStatusCode());
            return (AddressDTO) responseEntity.getBody();
        } else {
            log.error("customer service api call failed with error code: {}", responseEntity.getStatusCode());
            throw new AddressNotFoundException();
        }
    }

    public Mono<CustomerResponse> validateCustomerIdReactive(String customerId) {
        log.info("calling customer service api to validate customerId: {}", customerId);
        String accessToken = this.authenticationManager.getAccessToken();
        String baseUri = customerConfig.getServiceUri() + ApplicationConstant.CUSTOMER_SERVICE_API_BASE
                + ApplicationConstant.CUSTOMER_GET_CUSTOMER_URI + customerId;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ApplicationConstant.AUTHORIZATION, ApplicationConstant.TOKEN_PREFIX + accessToken);

        return webClient.get()
                .uri(baseUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(CustomerResponse.class);
                    } else {
                        log.error("customer service api call failed with error code: {}", response.statusCode());
                        throw new CustomerNotFoundException();
                    }
                });

    }

    public Mono<AddressDTO> validateAddressIdReactive(String addressId) {
        log.info("calling customer service api to validate addressId: {}", addressId);
        String accessToken = this.authenticationManager.getAccessToken();
        String baseUri = customerConfig.getServiceUri() + ApplicationConstant.ADDRESS_SERVICE_API_BASE
                + ApplicationConstant.CUSTOMER_GET_CUSTOMER_URI + addressId;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ApplicationConstant.AUTHORIZATION, ApplicationConstant.TOKEN_PREFIX + accessToken);
        return webClient.get()
                .uri(baseUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(AddressDTO.class);
                    } else {
                        log.error("customer service api call failed with error code: {}", response.statusCode());
                        throw new CustomerNotFoundException();
                    }
                });

    }


}
