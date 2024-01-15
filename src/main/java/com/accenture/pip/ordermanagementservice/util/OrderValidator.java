package com.accenture.pip.ordermanagementservice.util;


import com.accenture.pip.ordermanagementservice.dto.OrderDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderItemDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderStatus;
import com.accenture.pip.ordermanagementservice.dto.customer.AddressDTO;
import com.accenture.pip.ordermanagementservice.dto.customer.CustomerResponse;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.exception.AddressNotFoundException;
import com.accenture.pip.ordermanagementservice.exception.InvalidOrderFoodItemIdException;
import com.accenture.pip.ordermanagementservice.exception.OrderNotEditableException;
import com.accenture.pip.ordermanagementservice.exception.RestaurantNotFoundException;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class OrderValidator {

    @Autowired
    CustomerUtil customerUtil;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    FoodItemRepository foodItemRepository;

    public void validateOrderRequest(OrderDTO request) {
        validateCustomerDetails(request.getCustomerId(), request.getAddressId());
        validateRestaurantDetails(request.getRestaurantId(), request.getItems());
    }

    public void validateCustomerDetails(String customerId, String addressId) {
        Mono<CustomerResponse> customerResponse = customerUtil.validateCustomerIdReactive(customerId);
        Mono<AddressDTO> addressDTO = customerUtil.validateAddressIdReactive(addressId);
        customerResponse.subscribe(response -> {
            List<String> addressIdList = response.getAddressDTOs().stream().map(AddressDTO::getAddressId)
                    .toList();
            if (!addressIdList.contains(addressId)) {
                throw new AddressNotFoundException(new Throwable("Invalid addressId, address not registered with customer"));
            }
        });


    }

    public void validateOrderIsEditable(Order existingOrder) {

        LocalDateTime allowedTime = existingOrder.getCreatedAt().plusMinutes(5);
        // create a clock
        ZoneId zid = ZoneId.of("US/Eastern");
        LocalDateTime currentTime = LocalDateTime.now(zid);
        //LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(allowedTime)) {
            throw new OrderNotEditableException();
        }
    }

    public void validateRestaurantDetails(String restaurantId, List<OrderItemDTO> items) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (optionalRestaurant.isEmpty()) {
            throw new RestaurantNotFoundException();
        }
        List<String> foodItemIds = optionalRestaurant.get()
                .getFoodItemSet().stream()
                .map(FoodItem::getFoodItemId)
                .toList();
        List<String> orderFoodItemIds = items
                .stream()
                .map(OrderItemDTO::getFoodItemId)
                .toList();

        for (String orderFoodItemId : orderFoodItemIds) {
            if (!foodItemIds.contains(orderFoodItemId)) {
                throw new InvalidOrderFoodItemIdException(new Throwable("This food Id is not registered with restaurant"));
            }
        }
    }

    public void validateOrderStatus(Order existingOrder, String status) {
        String upperStatus = StringUtils.toRootUpperCase(status);
        try {
            if (StringUtils.isEmpty(OrderStatus.valueOf(upperStatus).toString())) {
                throw new InvalidOrderFoodItemIdException(
                        new Throwable("Valid status : PREPARING, READY, DELIVERED or CANCELED "));
            }
        } catch (IllegalArgumentException ex) {
            throw new InvalidOrderFoodItemIdException(
                    new Throwable("Valid status : PREPARING, READY, DELIVERED or CANCELED "));
        }
    }
}
