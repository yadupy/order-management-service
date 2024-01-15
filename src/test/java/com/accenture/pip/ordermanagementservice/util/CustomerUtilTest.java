package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.dto.customer.AddressDTO;
import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import com.accenture.pip.ordermanagementservice.exception.AddressNotFoundException;
import com.accenture.pip.ordermanagementservice.exception.CustomerNotFoundException;
import com.accenture.pip.ordermanagementservice.service.KeycloakAuthenticationManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CustomerUtilTest {

    @InjectMocks
    CustomerUtil customerUtil;

    @Mock
    CustomerUtil util;

    @Mock
    RestTemplate restTemplate;

    @Mock
    @Qualifier("keycloakWebClient")
    WebClient webClient;

    @Mock
    CustomerConfig customerConfig;

    @Mock
    KeycloakAuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String GOOD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";
    public static final String CUSTOMER_SERVICE_URI = "http://localhost:9090/customer-ws/customerManagement";

    @Test
    public void testValidateCustomerId_Success() {
        String customerId = UUID.randomUUID().toString();
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);
        when(customerConfig.getServiceUri()).thenReturn(CUSTOMER_SERVICE_URI);
        CustomerResponse response = createCustomer();

        Mockito.when(restTemplate.exchange(any(String.class), any(), any(), eq(CustomerResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        CustomerResponse customerResponse = customerUtil.validateCustomerId(customerId);
        Assertions.assertNotNull(customerResponse);


    }

    @Test
    public void testValidateCustomerId_Failure() {
        String customerId = UUID.randomUUID().toString();
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);
        when(customerConfig.getServiceUri()).thenReturn(CUSTOMER_SERVICE_URI);
        CustomerResponse response = createCustomer();

        Mockito.when(restTemplate.exchange(any(String.class), any(), any(), eq(CustomerResponse.class)))
                .thenThrow(new CustomerNotFoundException());
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerUtil.validateCustomerId(customerId));

    }

    @Test
    public void testValidateAddressId_Success() {
        String addressId = UUID.randomUUID().toString();
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);
        when(customerConfig.getServiceUri()).thenReturn(CUSTOMER_SERVICE_URI);
        AddressDTO addressDTO = addAddressDTO();

        Mockito.when(restTemplate.exchange(any(String.class), any(), any(), eq(AddressDTO.class)))
                .thenReturn(new ResponseEntity<>(addressDTO, HttpStatus.OK));
        AddressDTO addressResponse = customerUtil.validateAddressId(addressId);
        Assertions.assertNotNull(addressResponse);


    }

    @Test
    public void testValidateAddressId_Failure() {

        String addressId = UUID.randomUUID().toString();
        when(authenticationManager.getAccessToken()).thenReturn(GOOD_JWT);
        when(customerConfig.getServiceUri()).thenReturn(CUSTOMER_SERVICE_URI);
        AddressDTO addressDTO = addAddressDTO();

        Mockito.when(restTemplate.exchange(any(String.class), any(), any(), eq(CustomerResponse.class)))
                .thenThrow(new AddressNotFoundException());
        Assertions.assertThrows(AddressNotFoundException.class, () -> customerUtil.validateCustomerId(addressId));

    }



    public static CustomerResponse createCustomer() {
        AddressDTO address = addAddressDTO();

        CustomerResponse customer = CustomerResponse.builder()
                //.id(1)
                .id("d37291a7-9929-400d-9170-99aace2fb957")
                .firstName("naruto").lastName("uzumaki")
                .email("naruto.uzumaki@gmail.com")
                .contactNumber("9785481460")
                .addressDTOs(Set.of(address))
                .build();
        return customer;
    }

    public static AddressDTO addAddressDTO() {
        return AddressDTO
                .builder()
                .addressId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                //.id(1)
                .houseNo("b-402")
                .street("pratap raod")
                .city("gurugram")
                .state("haryana")
                .pinCode("122001")
                .build();
    }

}
