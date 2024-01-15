package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.dto.OrderItemDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderResponseDTO;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.OrderItem;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderUtil {

    public static Order createOrder() {
        OrderItem item = additems();

        Order order = Order.builder()
                //.id(1)
                .restaurantId("d37291a7-9929-400d-9170-99aace2fb957")
                .customerId("cc7291a7-9929-400d-9170-99aace2fb957")
                .items(Collections.singletonList(item))
                .addressId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                .total(1000.0)
                .build();
        item.setOrder(order);
        return order;
    }

    public static OrderItem additems() {
        OrderItem item = OrderItem.builder()
                .orderItemId("d37291a7-9929-400d-9170-99aace2fb957")
                .foodItemId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                .quantity(2)
                .build();
        return item;
    }

    public static OrderResponseDTO createOrderResponse(){
        OrderItemDTO item = addItemsDTO();
        return OrderResponseDTO.builder()
                .id(UUID.randomUUID().toString())
                .restaurantId("d37291a7-9929-400d-9170-99aace2fb957")
                .customerId("cc7291a7-9929-400d-9170-99aace2fb957")
                .items(Collections.singletonList(item))
                .addressId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                .total(1000.0)
                .build();
    }
    public static OrderResponseDTO createOrderResponseWithMultiItems(){
        List<OrderItemDTO> item = new ArrayList<>();
        item.add(addItemsDTO());
        item.add(addItemsDTO());

        return OrderResponseDTO.builder()
                .id(UUID.randomUUID().toString())
                .restaurantId("d37291a7-9929-400d-9170-99aace2fb957")
                .customerId("cc7291a7-9929-400d-9170-99aace2fb957")
                .items(item)
                .addressId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                .total(1000.0)
                .build();
    }

    public static OrderItemDTO addItemsDTO() {
        OrderItemDTO item = OrderItemDTO.builder()
                .foodItemId("3898db4e-fb79-43db-9b14-c902cc0040ab")
                .quantity(2)
                .build();
        return item;
    }

}
