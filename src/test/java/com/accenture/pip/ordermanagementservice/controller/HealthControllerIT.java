package com.accenture.pip.ordermanagementservice.controller;

import com.accenture.pip.ordermanagementservice.OrderManagementServiceApplication;
import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.dto.Health;
import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
import com.accenture.pip.ordermanagementservice.dto.OrderStatus;
import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import com.accenture.pip.ordermanagementservice.exception.AddressNotFoundException;
import com.accenture.pip.ordermanagementservice.exception.CustomerNotFoundException;
import com.accenture.pip.ordermanagementservice.service.ExternalHealthService;
import com.accenture.pip.ordermanagementservice.service.KeycloakAuthenticationManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {OrderManagementServiceApplication.class})
@TestPropertySource("classpath:application-test.properties")
public class HealthControllerIT {

    public static final HealthStatus DEFAULT_STATUS = HealthStatus.UP;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ExternalHealthService healthService;

    @Mock
    public RestTemplate restTemplate;

    @Autowired
    CustomerConfig customerConfig;

    @Mock
    KeycloakAuthenticationManager authenticationManager;

    private static final String GOOD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";
    @Test
    public void getHealthSuccess() throws Exception{

        when(restTemplate.exchange(any(String.class), any(), any(), eq(Health.class)))
                .thenReturn(new ResponseEntity<>(new Health(HealthStatus.UP), HttpStatus.OK));
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);

        mockMvc
                .perform(get("/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    public void getSubSystemHealthFailure() throws Exception{

        when(restTemplate.exchange(any(String.class), any(), any(), eq(Health.class)))
                .thenThrow(new RestClientException("Customer service not reachable"));
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);

        mockMvc
                .perform(get("/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.CustomerService.status").value("DOWN"));


    }
}
