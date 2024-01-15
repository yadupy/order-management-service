package com.accenture.pip.ordermanagementservice.controller;

import com.accenture.pip.ordermanagementservice.OrderManagementServiceApplication;
import com.accenture.pip.ordermanagementservice.dto.OrderStatus;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.OrderItem;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.mapper.OrderMapper;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.OrderRepository;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.accenture.pip.ordermanagementservice.service.OrderService;
import com.accenture.pip.ordermanagementservice.util.OrderUtil;
import com.accenture.pip.ordermanagementservice.util.OrderValidator;
import com.accenture.pip.ordermanagementservice.util.RestaurantUtil;
import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {OrderManagementServiceApplication.class})
@TestPropertySource("classpath:application-test.properties")
public class OrderControllerIT {

    @Autowired
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderValidator validator;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FoodItemRepository foodItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    private static final String GOOD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Test
    public void testAddOrderSuccess() throws Exception {
        String body = Resources.toString(Resources.getResource("payloads/addOrder.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/orders/create")
                .content(body)
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);
        doNothing().when(validator).validateCustomerDetails(any(String.class), any(String.class));
        doNothing().when(validator).validateRestaurantDetails(any(String.class), any(List.class));

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetOrderSuccess() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/get/" + savedOrder.getOrderId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void testGetAllOrders() throws Exception {

        Order order = OrderUtil.createOrder();

        MockHttpServletRequestBuilder request = get("/orders/getAll")
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        Order savedOrder = orderRepository.saveAndFlush(order);
        assertTrue(savedOrder.getId() != 0);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetOrderFailure() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/get/" + UUID.randomUUID().toString())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetAllCustomerOrdersSuccess() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/customerId/" + existingOrder.getCustomerId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetAllCustomerOrdersFailure() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/customerId/" + UUID.randomUUID().toString())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetAllOrdersForRestaurant() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/restaurantId/" + existingOrder.getRestaurantId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetAllOrdersForRestaurantFailure() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/restaurantId/" + UUID.randomUUID().toString())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetOrderWithSpecificFoodItemFailure() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/foodItemId/" + UUID.randomUUID().toString())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetOrderWithSpecificFoodItemSuccess() throws Exception {

        Restaurant restaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(savedRestaurant.getId() != 0);
        FoodItem savedFoodItem = savedRestaurant.getFoodItemSet().get(0);
        Order existingOrder = OrderUtil.createOrder();
        existingOrder.getItems().get(0).setFoodItemId(savedFoodItem.getFoodItemId());
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        String foodItemId = savedOrder.getItems().get(0).getFoodItemId();

        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = get("/orders/getAll/foodItemId/" + savedFoodItem.getFoodItemId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    //@Transactional
    public void testUpdateAddressByOrderId() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = patch("/orders/update/" + savedOrder.getOrderId() + "/address/" + savedOrder.getAddressId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        doNothing().when(validator).validateCustomerDetails(any(String.class), any(String.class));
        doNothing().when(validator).validateRestaurantDetails(any(String.class), any(List.class));

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
   // @Transactional
    public void testAddFoodItemByOrderId() throws Exception {
        String body = Resources.toString(Resources.getResource("payloads/updateOrder.json"), StandardCharsets.UTF_8);
        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = patch("/orders/update/" + savedOrder.getOrderId() + "/addItems")
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    //@Transactional
    public void testUpdateOrderStatus() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = patch("/orders/update/" + savedOrder.getOrderId() + "/status/" + OrderStatus.DELIVERED.toString())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    //@Transactional
    public void testDeleteOrder() throws Exception {

        Order existingOrder = OrderUtil.createOrder();
        Order savedOrder = orderRepository.saveAndFlush(existingOrder);
        assertTrue(savedOrder.getId() != 0);

        MockHttpServletRequestBuilder request = delete("/orders/delete/orderId/" + savedOrder.getOrderId())
                .header(TOKEN_HEADER, (TOKEN_PREFIX + " " + GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

}