package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.dto.FoodItemDTO;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {

    public static Restaurant createRestaurant(){
        FoodItem item = addFoodItem();

        Restaurant restaurant = Restaurant.builder()
                //.id(1)
                .restaurantId("d37291a7-9929-400d-9170-99aace2fb957")
                .restaurantName("kababs and curries")
                .email("kababsandcurries@gmail.com")
                .contactNumber("9785481460")
                .address("kababs and curries gurgaon")
                .foodItemSet(Collections.singletonList(item))
                .areaServes(List.of(122055))
                .build();
        item.setRestaurant(restaurant);

        return restaurant;
    }

    public static FoodItem addFoodItem(){
        return   FoodItem
                .builder()
                .foodItemId( "3898db4e-fb79-43db-9b14-c902cc0040ab")
                //.id(1)
                .Description("pizza")
                .name("pizza")
                .price(400.0)
                .build();
    }
    public static FoodItemDTO addFoodItemDTO(){
        return   FoodItemDTO
                .builder()
                .foodItemId( "3898db4e-fb79-43db-9b14-c902cc0040ab")
                //.id(1)
                .Description("pizza")
                .name("pizza")
                .price(400.0)
                .build();
    }
}
