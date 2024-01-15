package com.accenture.pip.ordermanagementservice.dto;

import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateRestaurantRequest {

    private List<Integer> areaServes = new ArrayList<>();
    private List<FoodItemDTO> foodItemSet = new ArrayList<>();

}
