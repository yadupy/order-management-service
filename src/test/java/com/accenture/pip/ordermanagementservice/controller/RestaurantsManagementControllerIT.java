package com.accenture.pip.ordermanagementservice.controller;


import com.accenture.pip.ordermanagementservice.OrderManagementServiceApplication;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.mapper.RestaurantMapper;
import com.accenture.pip.ordermanagementservice.repository.FoodItemRepository;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.accenture.pip.ordermanagementservice.service.RestaurantService;
import com.accenture.pip.ordermanagementservice.util.RestaurantUtil;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {OrderManagementServiceApplication.class})
@TestPropertySource("classpath:application-test.properties")
public class RestaurantsManagementControllerIT {


    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;


    @Autowired
    FoodItemRepository foodItemRepository;

    @Autowired
    RestaurantMapper mapper;


    @Autowired
    MockMvc mockMvc;


    private static final String GOOD_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJUMXBoanNldTlWNjVscnFHZWl";

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";


    @Test
    public void testAddRestaurantsSuccess() throws Exception {
        String body = Resources.toString(Resources.getResource("payloads/addRestaurant.json"), StandardCharsets.UTF_8);
        MockHttpServletRequestBuilder request = post("/restaurants/create")
                .content(body)
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        //when(restaurantRepository.saveAndFlush(any())).thenReturn(RestaurantUtil.createRestaurant());
        //when(restaurantRepository.findByEmailOrContactNumber(any(),any())).thenReturn(Optional.empty());

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetAllRestaurants() throws Exception {

        Restaurant restaurant = RestaurantUtil.createRestaurant();

        MockHttpServletRequestBuilder request = get("/restaurants/getAll")
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        Restaurant savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
        assertTrue(savedRestaurant.getId()!=0);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetRestaurantSuccess() throws Exception {

        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = get("/restaurants/get/"+savedRestaurant.getRestaurantId())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetRestaurantFailure() throws Exception {

        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = get("/restaurants/get/"+UUID.randomUUID().toString())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

 /*   @Test
    @Transactional
    public void testUpdateRestaurantSuccess() throws Exception {

        String body = Resources.toString(Resources.getResource("payloads/updateRestaurant.json"), StandardCharsets.UTF_8);
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = patch("/Restaurant/update/"+savedRestaurant.getRestaurantId())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }*/

    @Test
    @Transactional
    public void testUpdateRestaurantFailure() throws Exception {

        String body = Resources.toString(Resources.getResource("payloads/updateRestaurant.json"), StandardCharsets.UTF_8);
        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = patch("/restaurants/update/"+UUID.randomUUID().toString())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testDeleteRestaurantSuccess() throws Exception {

        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = delete("/restaurants/delete/restaurantId/"+savedRestaurant.getRestaurantId())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testDeleteRestaurantFailure() throws Exception {

        Restaurant existingRestaurant = RestaurantUtil.createRestaurant();
        Restaurant savedRestaurant= restaurantRepository.saveAndFlush(existingRestaurant);
        assertTrue(savedRestaurant.getId()!=0);

        MockHttpServletRequestBuilder request = delete("/Restaurant/delete/RestaurantId/"+UUID.randomUUID().toString())
                .header(TOKEN_HEADER,(TOKEN_PREFIX+" "+GOOD_JWT))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(status().isNotFound());
    }


  


}
