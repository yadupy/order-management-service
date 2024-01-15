package com.accenture.pip.ordermanagementservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class OrderItemDTO {

	@NotNull(message = "food product array cannot be null")
	private String foodItemId;

	@NotNull(message = "quantity is required")
	@Min(1)
	private Integer quantity;

}
