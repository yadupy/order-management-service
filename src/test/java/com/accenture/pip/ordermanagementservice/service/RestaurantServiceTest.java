package com.accenture.pip.ordermanagementservice.service;


import com.accenture.pip.ordermanagementservice.dto.RestaurantInfo;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.dto.UpdateRestaurantRequest;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.exception.EmailAlreadyInUseException;
import com.accenture.pip.ordermanagementservice.exception.RestaurantNotFoundException;
import com.accenture.pip.ordermanagementservice.mapper.RestaurantMapper;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.accenture.pip.ordermanagementservice.util.RestaurantUtil;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class RestaurantServiceTest {

    @InjectMocks
    RestaurantService managementService;
 
    @Mock
    RestaurantService restaurantService;
   
    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    RestaurantMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomerSuccess() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        String body = Resources.toString(Resources.getResource("payloads/addRestaurant.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();
        RestaurantInfo RestaurantInfo = gson.fromJson(body,RestaurantInfo.class);
        when(restaurantRepository.findByEmailOrContactNumber(any(),any())).thenReturn(Optional.empty());
        when(restaurantRepository.saveAndFlush(any())).thenReturn(existingRestaurant);
        when(mapper.restaurantRequestToRestaurant(any())).thenReturn(existingRestaurant);
        when(mapper.restaurantToRestaurantResponse(any())).thenReturn(response);
        RestaurantResponse savedRestaurantId  = managementService.createRestaurant(RestaurantInfo);
        Assertions.assertEquals(savedRestaurantId.getEmail(),existingRestaurant.getEmail());
        Assertions.assertEquals(savedRestaurantId.getContactNumber(),existingRestaurant.getContactNumber());
    }

    @Test
    public void testCreateCustomerFailure() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        String body = Resources.toString(Resources.getResource("payloads/addRestaurant.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();
        RestaurantInfo RestaurantInfo = gson.fromJson(body,RestaurantInfo.class);
        when(restaurantRepository.findByEmailOrContactNumber(any(),any())).thenReturn(Optional.of(existingRestaurant));

        Assertions.assertThrows(EmailAlreadyInUseException.class,()-> {
            managementService.createRestaurant(RestaurantInfo);
        });
    }
    @Test
    public void testGetAllCustomerSuccess() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();

        when(restaurantRepository.findAll()).thenReturn(List.of(existingRestaurant));
        when(mapper.restaurantToRestaurantResponse(any())).thenReturn(response);
        List<RestaurantResponse> savedRestaurantId  = managementService.getAllRestaurant();
        Assertions.assertFalse(savedRestaurantId.isEmpty());

    }

    @Test
    public void testGetCustomerSuccess() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();

        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.of(existingRestaurant));
        when(mapper.restaurantToRestaurantResponse(any())).thenReturn(response);

        RestaurantResponse savedRestaurantId  = managementService.getRestaurant(existingRestaurant.getRestaurantId());

        Assertions.assertFalse(savedRestaurantId.getContactNumber().isEmpty());

    }
    @Test
    public void testGetCustomerFailure() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();

        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantNotFoundException.class,()-> {
            managementService.getRestaurant(UUID.randomUUID().toString());
        });

    }

  /*  @Test
    public void testUpdateCustomerSuccess() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        String body = Resources.toString(Resources.getResource("payloads/updateRestaurant.json"), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        UpdateRestaurantRequest updateRestaurantRequest = gson.fromJson(body, UpdateRestaurantRequest.class);
        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .itemsServed(List.of(RestaurantUtil.addFoodItemDTO()))
                .build();


        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.of(existingRestaurant));
        when(restaurantService.updateExistingCustomer(any(),any())).thenReturn(RestaurantUtil.createRestaurant());
        when(restaurantRepository.saveAndFlush(any())).thenReturn(existingRestaurant);

        //when(restaurantService.)
        when(mapper.restaurantToRestaurantResponse(any())).thenReturn(response);

        RestaurantResponse savedRestaurantId  = managementService.updateRestaurant(existingRestaurant.getRestaurantId(),updateRestaurantRequest);
        Assertions.assertNotNull(savedRestaurantId);
        Assertions.assertFalse(savedRestaurantId.getItemsServed().isEmpty());


    }*/

    @Test
    public void testDeletedCustomerSuccess() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();

        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.of(existingRestaurant));

        managementService.deleteRestaurant(existingRestaurant.getRestaurantId());

        Mockito.verify(restaurantRepository, times(1)).delete(existingRestaurant);

    }

    @Test
    public void testDeletedCustomerFailure() throws IOException {
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();

        RestaurantResponse response = RestaurantResponse.builder()
                .id(UUID.randomUUID().toString())
                .email(existingRestaurant.getEmail())
                .contactNumber(existingRestaurant.getContactNumber())
                .build();

        when(restaurantRepository.findByRestaurantId(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantNotFoundException.class,()-> {
            managementService.deleteRestaurant(UUID.randomUUID().toString());
        });

    }

}
