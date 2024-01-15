package com.accenture.pip.ordermanagementservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Valid()
public class RestaurantInfo {

    @NotNull(message = "name is mandatory")
    public String name;

    @NotNull(message = "address is mandatory")
    public String address;

    @NotNull(message = "email is mandatory")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotNull(message = "contactNumber is mandatory")
    @Pattern(regexp="^[789]\\d{9}$")
    private String contactNumber;

    @NotNull
    private String imageUrl;

    private List<Integer> areaServes = new ArrayList<>();

    @JsonProperty("items")
    private List<FoodItemDTO> foodItemSet = new ArrayList<>();
}
