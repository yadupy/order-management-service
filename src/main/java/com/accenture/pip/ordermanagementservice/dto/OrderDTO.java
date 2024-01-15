package com.accenture.pip.ordermanagementservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OrderDTO {

    @JsonProperty(required = false)
	private OrderStatus status;

    @NotNull(message = "items is mandatory")
	private List<OrderItemDTO> items;

    @NotNull(message = "addressId is mandatory")
	private String addressId;

    @NotNull(message = "customerId is mandatory")
    private String customerId;

    @NotNull(message = "RestaurantId is mandatory")
    private String restaurantId;

    @JsonProperty(required = false)
	private Double total = 0.0d;

}
