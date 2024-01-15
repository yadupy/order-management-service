package com.accenture.pip.ordermanagementservice.mapper;


import com.accenture.pip.ordermanagementservice.dto.FoodItemDTO;
import com.accenture.pip.ordermanagementservice.dto.RestaurantInfo;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantMapper MAPPER = Mappers.getMapper( RestaurantMapper.class );

    @Mapping(target = "id", source = "restaurantId")
    @Mapping(target = "name", source = "restaurantName")
    @Mapping(target = "itemsServed", source = "foodItemSet")
    RestaurantResponse restaurantToRestaurantResponse(Restaurant entity);

    @Mapping(target = "restaurantId", source = "id")
    @Mapping(target = "restaurantName", source = "name")
    @Mapping(target = "foodItemSet", source = "itemsServed")
    Restaurant restaurantResponseToRestaurant(RestaurantResponse response);

    @Mapping(target = "restaurantName", source = "name")
    Restaurant restaurantRequestToRestaurant(RestaurantInfo request);

    FoodItem foodItemDtoToFoodItem(FoodItemDTO foodItemDTO);

    FoodItemDTO foodItemToFoodItemDTO(FoodItem foodItem);
}
