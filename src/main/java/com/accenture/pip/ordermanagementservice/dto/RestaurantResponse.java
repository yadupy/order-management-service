package com.accenture.pip.ordermanagementservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RestaurantResponse {

    public String id;
    public String name;
    public String address;
    private String email;
    private String contactNumber;
    private String imageUrl;
    private List<String> areaServes = new ArrayList<>();
    private List<FoodItemDTO> itemsServed = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
