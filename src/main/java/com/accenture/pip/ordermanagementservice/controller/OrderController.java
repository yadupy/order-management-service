package com.accenture.pip.ordermanagementservice.controller;


import com.accenture.pip.ordermanagementservice.dto.*;
import com.accenture.pip.ordermanagementservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/orders")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

	@PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "create a new order",
			description = "creates a new order from OrderDTO object")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public Mono<ResponseEntity<OrderResponseDTO>> addOrder(@RequestBody @Valid OrderDTO request)  {
		log.info("POST Method call received to create a new order with customerId: {}",request.getCustomerId());
        Mono<OrderResponseDTO> response = orderService.addOrder(request);
		return response.map(ResponseEntity::ok);
		//return ResponseEntity.ok(response);
	}

	@GetMapping("/get/{id}")
	@Operation(
			summary = "fetch order by id",
			description = "fetch order by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable("id") @ Valid @NotNull String orderId) {
		log.info("GET Method called to fetch order with orderId {}",orderId);
		OrderResponseDTO response  = orderService.getOrder(orderId);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/getAll")
	@Operation(
			summary = "fetch all orders",
			description = "fetch all orders")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public Mono<ResponseEntity<List<OrderResponseDTO>>> getAllOrders() {
		log.info("GET Method called to fetch all orders");
		Mono<List<OrderResponseDTO>> response  = orderService.getAllOrders();
		return response.map(ResponseEntity::ok);
	}

	@GetMapping(value = "/getAll/customerId/{customerId}")
	@Operation(
			summary = "fetch all orders for customerId",
			description = "fetch all orders for customerId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public Mono<ResponseEntity<List<OrderResponseDTO>>> getAllCustomerOrders(@PathVariable("customerId") @Valid @NotNull String customerId) {
		log.info("GET Method called to fetch all orders for customerId: {}",customerId);
		Mono<List<OrderResponseDTO>> response  = orderService.getAllCustomerOrders(customerId);
		return response.map(ResponseEntity::ok);
		//return ResponseEntity.ok(response);
	}

	@GetMapping(value ="/getAll/restaurantId/{restaurantId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "fetch all orders for restaurantId",
			description = "fetch all orders for restaurantId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public Mono<ResponseEntity<List<OrderResponseDTO>>> getAllOrdersForRestaurant(@PathVariable("restaurantId")
																				@Valid @NotNull String restaurantId) {
		log.info("GET Method called to fetch all orders for restaurantId: {}",restaurantId);
		Mono<List<OrderResponseDTO>> response  = orderService.getAllOrdersForRestaurant(restaurantId);
		return response.map(ResponseEntity::ok);
	}

@GetMapping("/getAll/foodItemId/{foodItemId}")
	@Operation(
			summary = "fetch all orders for foodItemId",
			description = "fetch all orders for foodItemId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public Mono<ResponseEntity<List<OrderResponseDTO>>> getOrderWithSpecificFoodItem(@PathVariable("foodItemId") @Valid @NotNull String foodItemId) {
		log.info("GET Method called to fetch all orders for foodItemId: {}",foodItemId);
		Mono<List<OrderResponseDTO>> response  = orderService.getOrderWithSpecificFoodItem(foodItemId);
		return response.map(ResponseEntity::ok);
	}


	@PatchMapping("/update/{orderId}/address/{addressId}")
	@Operation(
			summary = "update existing order",
			description = "update existing order based on orderId and addressId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public ResponseEntity<OrderResponseDTO> updateAddressByOrderId(@PathVariable("orderId") @Valid @NotNull String orderId,
																	 @PathVariable("addressId") @Valid @NotNull String addressId) {
		log.info("PATCH Method called to update addressId for an existing order with orderId :{}",orderId);
		OrderResponseDTO response = orderService.updateAddressByOrderId(orderId, addressId);
		return ResponseEntity.ok(response);
	}
	@PatchMapping("/update/{orderId}/addItems")
	@Operation(
			summary = "update existing order",
			description = "update existing order with new food items")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public ResponseEntity<OrderResponseDTO> addFoodItemByOrderId(@PathVariable("orderId") @Valid @NotNull String orderId,
																   @RequestBody  @Valid List<OrderItemDTO> items) {
		log.info("PATCH Method called to add total {} items for an existing order with orderId :{}",items.size(),orderId);
		OrderResponseDTO response = orderService.addFoodItemByOrderId(orderId,items);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/update/{orderId}/status/{status}")
	@Operation(
			summary = "update existing order",
			description = "update existing order status based on orderId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable("orderId") @Valid @NotNull String orderId,
																   @PathVariable("status") @Valid @NotNull String status) {
		log.info("PATCH Method called to update status for an existing order with orderId :{}",orderId);
		OrderResponseDTO response = orderService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete/orderId/{id}")
	@Operation(
			summary = "delete order by orderId",
			description = "delete order by orderId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400",description = "Bad Request"),
			@ApiResponse(responseCode = "404",description = "Resource Not found"),
			@ApiResponse(responseCode = "500",description = "Internal Server Error"),
	})
	@ResponseBody
	public ResponseEntity<String> deleteOrder(@PathVariable("id") String orderId) {
		log.info("DELETE Method called to delete order with orderId,{}",orderId);
		String message =  orderService.deleteOrder(orderId);
		return  ResponseEntity.ok(message);
	}
}
