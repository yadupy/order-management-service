package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.dto.*;

import com.accenture.pip.ordermanagementservice.entity.Order;

import com.accenture.pip.ordermanagementservice.exception.*;
import com.accenture.pip.ordermanagementservice.mapper.OrderMapper;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.OrderRepository;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.accenture.pip.ordermanagementservice.util.OrderUtil;
import com.accenture.pip.ordermanagementservice.util.OrderValidator;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;
    @Mock
    OrderService service;
    @Mock
    OrderValidator validator;
    @Mock
    OrderMapper orderMapper;
    @Mock
    OrderRepository orderRepository;
    @Mock
    FoodItemRepository foodItemRepository;
    @Mock
    RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testCreateOrderSuccess() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        String body = Resources.toString(Resources.getResource("payloads/addOrder.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        OrderDTO orderDTO = gson.fromJson(body, OrderDTO.class);
        doNothing().when(validator).validateOrderRequest(any());
        when(orderRepository.saveAndFlush(any())).thenReturn(existingOrder);
        when(orderMapper.orderDTOToOrder(any())).thenReturn(existingOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Mono<OrderResponseDTO> responseDTO  = orderService.addOrder(orderDTO);
        Assertions.assertEquals(responseDTO.block(),response);

    }

   @Test
    public void testCreateCustomerFailure() throws IOException {
       Order existingOrder = OrderUtil.createOrder();
       String body = Resources.toString(Resources.getResource("payloads/addOrder.json"), StandardCharsets.UTF_8);
       Gson gson = new Gson();
       OrderResponseDTO response = OrderUtil.createOrderResponse();
       OrderDTO orderDTO = gson.fromJson(body, OrderDTO.class);
       doThrow(AddressNotFoundException.class).when(validator).validateOrderRequest(any());
       when(orderRepository.saveAndFlush(any())).thenReturn(existingOrder);
       when(orderMapper.orderDTOToOrder(any())).thenReturn(existingOrder);
       when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
       Assertions.assertThrows(AddressNotFoundException.class, ()-> orderService.addOrder(orderDTO));

   }

    @Test
    public void testCreateCustomerFailure_CustomerNotFoundException() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        String body = Resources.toString(Resources.getResource("payloads/addOrder.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        OrderDTO orderDTO = gson.fromJson(body, OrderDTO.class);
        doThrow(CustomerNotFoundException.class).when(validator).validateOrderRequest(any());
        when(orderRepository.saveAndFlush(any())).thenReturn(existingOrder);
        when(orderMapper.orderDTOToOrder(any())).thenReturn(existingOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(CustomerNotFoundException.class, ()-> orderService.addOrder(orderDTO));

    }


    @Test
    public void testCreateCustomerFailure_RestaurantNotFoundException() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        String body = Resources.toString(Resources.getResource("payloads/addOrder.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        OrderDTO orderDTO = gson.fromJson(body, OrderDTO.class);
        doThrow(RestaurantNotFoundException.class).when(validator).validateOrderRequest(any());
        when(orderRepository.saveAndFlush(any())).thenReturn(existingOrder);
        when(orderMapper.orderDTOToOrder(any())).thenReturn(existingOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(RestaurantNotFoundException.class, ()-> orderService.addOrder(orderDTO));

    }



    @Test
    public void testGetAllOrderSuccess() throws IOException {
        Order existingOrder = OrderUtil.createOrder();

        OrderResponseDTO response = OrderUtil.createOrderResponse();

        when(orderRepository.findAll()).thenReturn(List.of(existingOrder));
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Mono<List<OrderResponseDTO>> savedOrder  = orderService.getAllOrders();
        Assertions.assertEquals(savedOrder.block().size(),1);

    }


    @Test
    public void testGetOrderByIdSuccess() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        OrderResponseDTO savedOrder = orderService.getOrder(existingOrder.getOrderId());
        Assertions.assertEquals(savedOrder.getCustomerId(),response.getCustomerId());
        Assertions.assertEquals(savedOrder.getId(),response.getId());

    }
    @Test
    public void testGetOrderByIdFailure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.findByOrderId(any())).thenThrow(new OrderNotFoundException());
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(OrderNotFoundException.class, ()-> orderService.getOrder(existingOrder.getOrderId()));

    }

  @Test
    public void testGetAllCustomerOrdersSuccess() throws IOException {
      Order existingOrder = OrderUtil.createOrder();
      OrderResponseDTO response = OrderUtil.createOrderResponse();
      when(orderRepository.findAllByCustomerId(any())).thenReturn(List.of(existingOrder));
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
      Mono<List<OrderResponseDTO>> savedOrder = orderService.getAllCustomerOrders(existingOrder.getCustomerId());
      Assertions.assertEquals(savedOrder.block().size(),1);

    }
    @Test
    public void testGetAllCustomerOrdersFailure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.findAllByCustomerId(any())).thenThrow(new OrderNotFoundException());
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(OrderNotFoundException.class, ()-> orderService.getAllCustomerOrders(existingOrder.getCustomerId()));

    }

    @Test
    public void testGetAllOrdersForRestaurantSuccess() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.findAllByRestaurantId(any())).thenReturn(List.of(existingOrder));
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Mono<List<OrderResponseDTO>> savedOrder = orderService.getAllOrdersForRestaurant(existingOrder.getRestaurantId());
        Assertions.assertEquals(savedOrder.block().size(),1);

    }
    @Test
    public void testGetAllOrdersForRestaurantFailure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.findAllByRestaurantId(any())).thenThrow(new OrderNotFoundException());
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(OrderNotFoundException.class, ()-> orderService.getAllCustomerOrders(existingOrder.getRestaurantId()));

    }


    @Test
    public void testUpdateAddressByOrderId_Success() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        Order updatedOrder = OrderUtil.createOrder();
        String newAddressId= UUID.randomUUID().toString();
        updatedOrder.setAddressId(newAddressId);
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        response.setAddressId(newAddressId);

        doNothing().when(validator).validateCustomerDetails(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any())).thenReturn(updatedOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);


        OrderResponseDTO savedOrder  = orderService.updateAddressByOrderId(existingOrder.getOrderId(),newAddressId);
        Assertions.assertNotNull(savedOrder);
        Assertions.assertEquals(savedOrder.getAddressId(),newAddressId);


    }

    @Test
    public void testUpdateAddressByOrderId_Failure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();

        OrderResponseDTO response = OrderUtil.createOrderResponse();

        doThrow(AddressNotFoundException.class).when(validator).validateCustomerDetails(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));

        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        String newAddressId= UUID.randomUUID().toString();
        Assertions.assertThrows(AddressNotFoundException.class, ()-> orderService.updateAddressByOrderId(existingOrder.getOrderId(),newAddressId));


    }

   /* @Test
    public void testAddFoodItemByOrderId_Success() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        Order updatedOrder = OrderUtil.createOrder();


        OrderResponseDTO responseDTO = OrderUtil.createOrderResponseWithMultiItems();
        FoodItem item = RestaurantUtil.addFoodItem();


        OrderItemDTO itemDTO = OrderUtil.addItemsDTO();

        doNothing().when(validator).validateRestaurantDetails(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));
        when(foodItemRepository.findAllByFoodItemIdIn(any())).thenReturn(List.of(item));
        when(orderRepository.save(any())).thenReturn(updatedOrder);
        when(orderMapper.orderItemDTOToOrderItem(any())).thenReturn(OrderUtil.additems());
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(responseDTO);

        OrderResponseDTO updatedOrderResponse  = orderService.addFoodItemByOrderId(existingOrder.getOrderId(),List.of(itemDTO));

        Assertions.assertNotNull(updatedOrderResponse);
        Assertions.assertEquals(updatedOrderResponse.getItems().size(),2);


    }*/

    @Test
    public void testAddFoodItemByOrderId_Failure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        String body = Resources.toString(Resources.getResource("payloads/updateOrder.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        List itemDTOList = gson.fromJson(body, List.class);

        OrderResponseDTO response = OrderUtil.createOrderResponse();

        doThrow(InvalidOrderFoodItemIdException.class).when(validator).validateRestaurantDetails(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));

        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        String newAddressId= UUID.randomUUID().toString();
        Assertions.assertThrows(InvalidOrderFoodItemIdException.class, ()-> orderService.addFoodItemByOrderId(existingOrder.getOrderId(),itemDTOList));


    }
    @Test
    public void testUpdateOrderStatus_Success() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        Order updatedOrder = OrderUtil.createOrder();
        updatedOrder.setStatus(OrderStatus.DELIVERED.toString());
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        response.setStatus(OrderStatus.DELIVERED);

        doNothing().when(validator).validateOrderStatus(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any())).thenReturn(existingOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);


        OrderResponseDTO savedOrder  = orderService.updateOrderStatus(existingOrder.getOrderId(),OrderStatus.DELIVERED.toString());
        Assertions.assertNotNull(savedOrder);
        Assertions.assertEquals(savedOrder.getStatus().toString(),OrderStatus.DELIVERED.toString());


    }

    @Test
    public void testUpdateOrderStatus_Failure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();

        OrderResponseDTO response = OrderUtil.createOrderResponse();


        doThrow(InvalidOrderStatusException.class).when(validator).validateOrderStatus(any(),any());
        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));

        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        String newAddressId= UUID.randomUUID().toString();
        Assertions.assertThrows(InvalidOrderStatusException.class, ()-> orderService.updateOrderStatus(existingOrder.getOrderId(),OrderStatus.DELIVERED.toString()));


    }

    @Test
    public void testDeleteOrder_Success() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        Order updatedOrder = OrderUtil.createOrder();
        updatedOrder.setStatus(OrderStatus.CANCELED.toString());
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        response.setStatus(OrderStatus.CANCELED);

        when(orderRepository.findByOrderId(any())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any())).thenReturn(updatedOrder);
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);


        String savedOrder  = orderService.deleteOrder(existingOrder.getOrderId());
        Assertions.assertNotNull(savedOrder);
        Assertions.assertTrue(savedOrder.contains("cancelled order"));


    }

    @Test
    public void testDeleteOrder_Failure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();

        OrderResponseDTO response = OrderUtil.createOrderResponse();

        when(orderRepository.findByOrderId(any())).thenThrow(new OrderNotFoundException());

        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        String newAddressId= UUID.randomUUID().toString();
        Assertions.assertThrows(OrderNotFoundException.class, ()-> orderService.deleteOrder(existingOrder.getOrderId()));


    }

    @Test
    public void testGetOrderWithSpecificFoodItem_Success() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.getAllByFoodItem(any())).thenReturn(List.of(existingOrder));
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Mono<List<OrderResponseDTO>> savedOrder = orderService.getOrderWithSpecificFoodItem(existingOrder.getItems().get(0).getFoodItemId());
        Assertions.assertEquals(savedOrder.block().size(),1);

    }
    @Test
    public void testGetOrderWithSpecificFoodItem_Failure() throws IOException {
        Order existingOrder = OrderUtil.createOrder();
        OrderResponseDTO response = OrderUtil.createOrderResponse();
        when(orderRepository.getAllByFoodItem(any())).thenThrow(new OrderNotFoundException());
        when(orderMapper.orderToOrderResponseDTO(any())).thenReturn(response);
        Assertions.assertThrows(OrderNotFoundException.class, ()-> orderService.getOrderWithSpecificFoodItem(UUID.randomUUID().toString()));

    }


}
