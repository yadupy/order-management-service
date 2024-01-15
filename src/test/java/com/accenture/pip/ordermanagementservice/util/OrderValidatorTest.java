package com.accenture.pip.ordermanagementservice.util;

import com.accenture.pip.ordermanagementservice.dto.OrderDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderItemDTO;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.exception.CustomerNotFoundException;
import com.accenture.pip.ordermanagementservice.exception.RestaurantNotFoundException;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OrderValidatorTest {

    @InjectMocks
    OrderValidator orderValidator;
    @Mock
    OrderValidator validator;
    @Mock
    CustomerUtil customerUtil;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    FoodItemRepository foodItemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateOrderIsEditable(){
        Order existingOrder = OrderUtil.createOrder();
        existingOrder.setCreatedAt(LocalDateTime.now());
        existingOrder.setUpdatedAt(LocalDateTime.now());
        validator.validateOrderIsEditable(existingOrder);
        Errors errors = new BindException(existingOrder,"existingOrder");
        Assertions.assertEquals(Boolean.TRUE,errors.getAllErrors().isEmpty());

    }

    @Test
    public void testValidateRestaurantDetails(){
        String restaurantId = UUID.randomUUID().toString();
        OrderItemDTO itemDTO = OrderUtil.addItemsDTO();
        Restaurant restaurant = RestaurantUtil.createRestaurant();
        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.of(restaurant));
        //Order existingOrder = OrderUtil.createOrder();
        validator.validateRestaurantDetails(restaurantId, List.of(itemDTO));
        Errors errors = new BindException(itemDTO,"itemDTO");
        Assertions.assertEquals(Boolean.TRUE,errors.getAllErrors().isEmpty());

    }


}
