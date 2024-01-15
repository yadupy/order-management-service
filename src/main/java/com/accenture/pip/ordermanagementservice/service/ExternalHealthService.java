package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.config.CustomerConfig;
import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import com.accenture.pip.ordermanagementservice.dto.Health;
import com.accenture.pip.ordermanagementservice.dto.OrderHealth;
import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import java.net.http.HttpHeaders;

@Service
public class ExternalHealthService {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    CustomerConfig customerConfig;

    @Autowired
    KeycloakAuthenticationManager authenticationManager;

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "customerFallback")
    public ResponseEntity<Health> getCustomerServiceHealth() {
        String healthUri = customerConfig.getServiceUri() + "/health";
        String accessToken = this.authenticationManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(ApplicationConstant.AUTHORIZATION,ApplicationConstant.TOKEN_PREFIX+accessToken);
        HttpEntity entity = new HttpEntity(null, headers);
        return restTemplate.exchange(healthUri, HttpMethod.GET, entity, Health.class);
    }

    private ResponseEntity<Object> customerFallback(Exception e) {
        return new ResponseEntity<Object>(new Health(HealthStatus.DOWN), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}