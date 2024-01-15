package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.dto.*;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.OrderItem;
import com.accenture.pip.ordermanagementservice.exception.*;
import com.accenture.pip.ordermanagementservice.mapper.OrderMapper;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.OrderRepository;
import com.accenture.pip.ordermanagementservice.util.OrderValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderValidator validator;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FoodItemRepository foodItemRepository;

    @Transactional
    public Mono<OrderResponseDTO> addOrder(OrderDTO request) {
        log.info("Service method saveOrder reached for customerId: {}", request.getCustomerId());
        log.info("validating new order request");
        validator.validateOrderRequest(request);

        Order newOrder = orderMapper.orderDTOToOrder(request);
        List<OrderItem> itemList = newOrder.getItems();
        itemList.stream().forEach(item -> item.setOrder(newOrder));
        Map<String, Integer> foodItemQuantityMap = request.getItems()
                .stream()
                .collect(
                        Collectors.toMap(OrderItemDTO::getFoodItemId, OrderItemDTO::getQuantity));

        List<FoodItem> foodItems = foodItemRepository.findAllByFoodItemIdIn(foodItemQuantityMap.keySet().stream().toList());
        Double totalAmount = 0.0;
        if (request.getTotal() <= 0) {
            totalAmount = calculateTotalAmount(foodItemQuantityMap, foodItems);
        }
        newOrder.setTotal(totalAmount);
        log.info("total amount calculated for the order: {}", totalAmount);
        Order savedOrder = orderRepository.saveAndFlush(newOrder);
        log.info("new order created with orderId :{}", savedOrder.getOrderId());
        return Mono.just(orderMapper.orderToOrderResponseDTO(savedOrder));
    }

    @Transactional
    public OrderResponseDTO getOrder(String orderId) {
        Order existingOrder = getOrderById(orderId);
        return orderMapper.orderToOrderResponseDTO(existingOrder);

    }


    private Order getOrderById(String orderId) {
        log.info("fetching records for the orderId with orderId {}", orderId);
        Optional<Order> existingOrder = orderRepository.findByOrderId(orderId);
        if (!existingOrder.isPresent()) {
            throw new OrderNotFoundException(new Throwable("Invalid orderId"));
        }
        log.info("found order with orderId {}", orderId);
        return existingOrder.get();
    }


    @Transactional
    public Mono<List<OrderResponseDTO>> getAllOrders() {

        log.info("fetching all the orders from database");
        List<OrderResponseDTO> orderList = new ArrayList<>();
        orderList.addAll(orderRepository.findAll()
                .stream()
                .map(order -> orderMapper.orderToOrderResponseDTO(order))
                .toList());
        log.info("found total {} orders from the records", orderList.size());

        return Mono.just(orderList);
    }


    @Transactional
    public Mono<List<OrderResponseDTO>> getAllCustomerOrders(String customerId) {
        log.info("fetching all the orders from database for customerId: {}", customerId);
        List<OrderResponseDTO> orderList = new ArrayList<>();
        List<Order> customerOrders = orderRepository.findAllByCustomerId(customerId);
        if (customerOrders == null || customerOrders.isEmpty()) {
            log.error("No order found for customer, Invalid customerId: {}", customerId);
            throw new OrderNotFoundException(new Throwable("No order found for customer, Invalid customerId: " + customerId));
        }
        orderList.addAll(customerOrders
                .stream()
                .map(order -> orderMapper.orderToOrderResponseDTO(order))
                .toList());
        log.info("found total {} orders for the customer :{}", orderList.size(), customerId);

        return Mono.just(orderList);
    }

    @Transactional
    public Mono<List<OrderResponseDTO>> getAllOrdersForRestaurant(String restaurantId) {
        log.info("fetching all the orders from database for restaurantId: {}", restaurantId);
        List<OrderResponseDTO> orderList = new ArrayList<>();
        List<Order> restaurantOrders = orderRepository.findAllByRestaurantId(restaurantId);
        if (restaurantOrders == null || restaurantOrders.isEmpty()) {
            log.error("No order found for customer, Invalid restaurantId: {}", restaurantId);
            throw new OrderNotFoundException(new Throwable("No order found for restaurant, Invalid restaurantId: " + restaurantId));
        }
        orderList.addAll(restaurantOrders
                .stream()
                .map(order -> orderMapper.orderToOrderResponseDTO(order))
                .toList());
        log.info("found total {} orders for the restaurant :{}", orderList.size(), restaurantId);

        return Mono.just(orderList);
    }

    @Transactional
    public OrderResponseDTO updateAddressByOrderId(String orderId, String addressId) {
        Order existingOrder = getOrderById(orderId);
        validator.validateOrderIsEditable(existingOrder);
        validator.validateCustomerDetails(existingOrder.getCustomerId(), existingOrder.getAddressId());
        existingOrder.setAddressId(addressId);
        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("order {} updated with addressId {}", updatedOrder.getOrderId(), updatedOrder.getAddressId());
        return orderMapper.orderToOrderResponseDTO(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO addFoodItemByOrderId(String orderId, List<OrderItemDTO> itemDTOList) {
        Order existingOrder = getOrderById(orderId);
        validator.validateOrderIsEditable(existingOrder);
        validator.validateRestaurantDetails(existingOrder.getRestaurantId(), itemDTOList);

        List<OrderItem> items = itemDTOList.stream()
                .map(item -> orderMapper.orderItemDTOToOrderItem(item))
                .toList();
        items.stream().forEach(item -> item.setOrder(existingOrder));
        existingOrder.getItems().addAll(items);
        Map<String, Integer> foodItemQuantityMap = itemDTOList.stream()
                .collect(
                        Collectors.toMap(OrderItemDTO::getFoodItemId, OrderItemDTO::getQuantity));

        List<FoodItem> foodItems = foodItemRepository.findAllByFoodItemIdIn(foodItemQuantityMap.keySet().stream().toList());

        //Double totalAmount = calculateTotalAmount(foodItemQuantityMap, foodItems);
        Double newBill = calculateTotalAmount(foodItemQuantityMap, foodItems);
        Double totalAmount = Double.sum(existingOrder.getTotal(), newBill);
        existingOrder.setTotal(totalAmount);
        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("order {} added with total {} items", updatedOrder.getOrderId(), items.size());
        return orderMapper.orderToOrderResponseDTO(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(String orderId, String status) {
        Order existingOrder = getOrderById(orderId);
        validator.validateOrderIsEditable(existingOrder);
        validator.validateOrderStatus(existingOrder, status);
        existingOrder.setStatus(StringUtils.toRootUpperCase(status));
        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("order with orderId {} updated to status {}", updatedOrder.getOrderId(), updatedOrder.getStatus());
        return orderMapper.orderToOrderResponseDTO(updatedOrder);

    }

    @Transactional
    public String deleteOrder(String orderId) {
        Order existingOrder = getOrderById(orderId);
        validator.validateOrderIsEditable(existingOrder);
        existingOrder.setStatus(OrderStatus.CANCELED.toString());
        Order updatedOrder = orderRepository.save(existingOrder);
        return "cancelled order :" + orderId;
    }

    private Double calculateTotalAmount(Map<String, Integer> foodItemQuantityMap, List<FoodItem> foodItems) {
        double totalAmount = 0.0d;
        for (FoodItem item : foodItems) {
            if (foodItemQuantityMap.containsKey(item.getFoodItemId())) {
                totalAmount += item.getPrice() * foodItemQuantityMap.get(item.getFoodItemId());
            }
        }
        return totalAmount;
    }


    public Mono<List<OrderResponseDTO>> getOrderWithSpecificFoodItem(String foodItemId) {
        log.info("fetching all the orders from database for foodItemId: {}", foodItemId);
        List<OrderResponseDTO> orderList = new ArrayList<>();
        List<Order> orders = orderRepository.getAllByFoodItem(foodItemId);
        if (orders == null || orders.isEmpty()) {
            log.error("No order found for foodItem, Invalid foodItemId: {}", foodItemId);
            throw new OrderNotFoundException(new Throwable("No order found for this foodItem, Invalid foodItemId: " + foodItemId));
        }
        orderList.addAll(orders
                .stream()
                .map(order -> orderMapper.orderToOrderResponseDTO(order))
                .toList());
        log.info("found total {} orders for the foodItemId :{}", orderList.size(), foodItemId);
        return Mono.just(orderList);
    }

}
