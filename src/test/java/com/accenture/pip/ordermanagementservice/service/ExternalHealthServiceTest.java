package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.dto.Health;
import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.exception.RestaurantNotFoundException;
import com.accenture.pip.ordermanagementservice.util.RestaurantUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ExternalHealthServiceTest {

    @InjectMocks
    ExternalHealthService externalHealthService;

    @Mock
    public RestTemplate restTemplate;

    @Mock
    CustomerConfig customerConfig;

    @Mock
    KeycloakAuthenticationManager authenticationManager;

    private static final String GOOD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomerServiceHealth_Success() throws IOException {

       // when(restTemplate.exchange(any(String.class), any(), any(), eq(Health.class)))
         //       .thenThrow(new RestClientException("Customer service not reachable"));

        when(restTemplate.exchange(any(String.class), any(), any(), eq(Health.class)))
                .thenReturn(new ResponseEntity<>(new Health(HealthStatus.UP), HttpStatus.OK));
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);
        ResponseEntity<Health> response  = externalHealthService.getCustomerServiceHealth();
        Health customerHealth= response.getBody();
        Assertions.assertEquals(customerHealth.getHealthStatus(),HealthStatus.UP);

    }

    @Test
    public void testGetCustomerServiceHealth_Failure() throws IOException {

         when(restTemplate.exchange(any(String.class), any(), any(), eq(Health.class)))
               .thenThrow(new RestClientException("Customer service not reachable"));

        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);

        Assertions.assertThrows(RestClientException.class,()-> {
            externalHealthService.getCustomerServiceHealth();
        });
    }

}
