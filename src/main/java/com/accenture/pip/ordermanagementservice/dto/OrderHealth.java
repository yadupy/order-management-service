package com.accenture.pip.ordermanagementservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHealth {

    @JsonProperty("status")
    HealthStatus healthStatus;

    @JsonProperty("CustomerService")
    Health serviceHealth;
}
