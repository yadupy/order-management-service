package com.accenture.pip.ordermanagementservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;


@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FoodItemDTO {

    @NotNull(message = "foodItemId is mandatory")
    private String foodItemId;

    @NotNull(message = "name is mandatory")
    private String name;

    @NotNull(message = "Description is mandatory")
    private String Description;

    @NotNull(message = "price is mandatory")
    private Double price;

    //@NotNull(message = "Description is mandatory")
    private String imageUrl;

}
