package com.accenture.pip.ordermanagementservice.controller;


import com.accenture.pip.ordermanagementservice.dto.RestaurantInfo;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.dto.UpdateRestaurantRequest;
import com.accenture.pip.ordermanagementservice.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/restaurants")
@Validated
@Tag(name = "Restaurant Management Api", description = "This api is used to manage restaurant records")

public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/create")
    @Operation(
            summary = "create a new restaurant",
            description = "creates a new restaurant from RestaurantInfo object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    @ResponseBody
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody @Valid @NotNull  RestaurantInfo request) {
        log.info("POST Method called to create restaurant with email address:{}",request.getEmail());
        RestaurantResponse restaurantResponse = restaurantService.createRestaurant(request);
        return  ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping("/getAll")
    @Operation(
            summary = "fetches all the restaurants",
            description = "fetches all the restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    @ResponseBody
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurant(){
        log.info("GET Method called to fetch all the restaurants");
        List<RestaurantResponse> restaurantResponseList = restaurantService.getAllRestaurant();
        return ResponseEntity.ok(restaurantResponseList);
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "fetch restaurant by id",
            description = "fetch restaurant by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    @ResponseBody
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable("id") @Valid @NotNull String restaurantId){
        log.info("GET Method called to fetch restaurant with restaurantId {}",restaurantId);
        RestaurantResponse customerResponse  = restaurantService.getRestaurant(restaurantId);
        return ResponseEntity.ok(customerResponse);
    }

    @PatchMapping("/update/{id}")
    @Operation(
            summary = "update existing restaurant",
            description = "update existing restaurant based on customerId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    @ResponseBody
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable("id") @Valid @NotNull String restaurantId,
                                                                  @RequestBody  @Valid UpdateRestaurantRequest request) {
        log.info("PUT Method called to update an existing restaurant with restaurantId :{}",restaurantId);
        RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, request);
                return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/restaurantId/{id}")
    @Operation(
            summary = "delete restaurant by restaurantId",
            description = "delete restaurant by restaurantId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad Request"),
            @ApiResponse(responseCode = "404",description = "Resource Not found"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error"),
    })
    @ResponseBody
    public ResponseEntity<String> deleteRestaurant(@PathVariable("id") @Valid @NotNull String restaurantId) {
        log.info("DELETE Method called to delete restaurant with restaurantId,{}",restaurantId);
        String message =  restaurantService.deleteRestaurant(restaurantId);
        return  ResponseEntity.ok(message);
    }
}
