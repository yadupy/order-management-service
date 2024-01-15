package com.accenture.pip.ordermanagementservice.controller;


import com.accenture.pip.ordermanagementservice.dto.Health;
import com.accenture.pip.ordermanagementservice.dto.OrderHealth;
import com.accenture.pip.ordermanagementservice.dto.HealthStatus;
import com.accenture.pip.ordermanagementservice.service.ExternalHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/health")
public class HealthController {

    @Autowired
    ExternalHealthService healthService;
    @GetMapping
    @Operation(
            summary = "checks system health",
            description = "checks system health")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    public ResponseEntity<OrderHealth> getHealth(){
        log.info("GET Method called to get current health of the service");
        OrderHealth health = new OrderHealth();
        health.setHealthStatus(HealthStatus.UP);
        Health customerServiceHealth =  healthService.getCustomerServiceHealth().getBody();
        health.setServiceHealth(customerServiceHealth);
        return ResponseEntity.ok(health);
    }
}
