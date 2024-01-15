package com.accenture.pip.ordermanagementservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDTO {


    public String id;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private String addressId;
    private String customerId;
    private String restaurantId;
    private Double total = 0.0d;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
