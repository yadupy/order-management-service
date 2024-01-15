package com.accenture.pip.ordermanagementservice.service;

import com.accenture.pip.ordermanagementservice.dto.FoodItemDTO;
import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import com.accenture.pip.ordermanagementservice.mapper.RestaurantMapper;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.accenture.pip.ordermanagementservice.dto.RestaurantInfo;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.dto.UpdateRestaurantRequest;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import com.accenture.pip.ordermanagementservice.exception.ContactNumberAlreadyInUseException;
import com.accenture.pip.ordermanagementservice.exception.EmailAlreadyInUseException;
import com.accenture.pip.ordermanagementservice.exception.RestaurantNotFoundException;
import com.accenture.pip.ordermanagementservice.mapper.RestaurantMapper;
import com.accenture.pip.ordermanagementservice.repository.RestaurantRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantMapper mapper;

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantInfo request) {
        log.info("checking if the email address is already in registered");
        Optional<Restaurant> restaurant  = restaurantRepository.findByEmailOrContactNumber(request.getEmail(),request.getContactNumber());
        if(restaurant.isPresent()){
            log.error("restaurant already exists, rejecting new restaurant creation");
            if(restaurant.get().getEmail().equalsIgnoreCase(request.getEmail())) {
                log.info("new restaurant can't be created, since email : {} already in use", request.getEmail());
                throw new EmailAlreadyInUseException();
            }
            else {
                log.info("new restaurant can't be created, since contact number : {} already in use", request.getContactNumber());
                throw new ContactNumberAlreadyInUseException();
            }
        }
        else{
            Restaurant newrestaurant  = mapper.restaurantRequestToRestaurant(request);
            List<FoodItem> foodItems = newrestaurant.getFoodItemSet();
            foodItems.stream()
                    .forEach(foodItem -> foodItem.setRestaurant(newrestaurant));
            Restaurant createdrestaurant = restaurantRepository.saveAndFlush(newrestaurant);
            log.info("crated a new restaurant with email : {} and id: {}",createdrestaurant.getEmail(), createdrestaurant.getRestaurantId());
            return mapper.restaurantToRestaurantResponse(createdrestaurant);
        }
    }

    @Transactional
    public RestaurantResponse updateRestaurant(String restaurantId, UpdateRestaurantRequest request) {

        log.info("fetching records for the restaurant with restaurantId {}", restaurantId);
        Optional<Restaurant> existingRestaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(!existingRestaurant.isPresent()){
            throw new RestaurantNotFoundException(new Throwable("Invalid restaurantId"));
        }
        log.info("found restaurant with email {} and restaurantId {}", existingRestaurant.get().getEmail(), restaurantId);

        Restaurant updatedRestaurant =  restaurantRepository.save(updateExistingCustomer(existingRestaurant,request));
        log.info("updated existing restaurant with email address: {} and restaurantId {}"
                ,updatedRestaurant.getEmail(), updatedRestaurant.getRestaurantId());

        return  mapper.restaurantToRestaurantResponse(updatedRestaurant);


    }

    @Transactional
    public Restaurant updateExistingCustomer(Optional<Restaurant> existingRestaurant, UpdateRestaurantRequest request){
        Restaurant newRestaurant = existingRestaurant.get();

        if(request.getAreaServes()!=null) {
            newRestaurant.getAreaServes().addAll(request.getAreaServes());
        }
        if(request.getFoodItemSet()!=null) {
            List<FoodItemDTO> itemDTOSet =  request.getFoodItemSet();
            List<FoodItem> items =  new ArrayList<>();
            for(FoodItemDTO itemDTO:itemDTOSet){
               FoodItem item =  mapper.foodItemDtoToFoodItem(itemDTO);
               item.setRestaurant(newRestaurant);
               items.add(item);
            }
            newRestaurant.getFoodItemSet().addAll(items);
        }

        return newRestaurant;
    }

    @Transactional
    public String deleteRestaurant(String restaurantId) {

        log.info("fetching records for the restaurant with restaurantId {}", restaurantId);
        Optional<Restaurant> existingRestaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(!existingRestaurant.isPresent()){
            throw new RestaurantNotFoundException(new Throwable("Invalid restaurantId"));
        }
        log.info("found restaurantId with email {} and restaurantId {}", existingRestaurant.get().getEmail(), restaurantId);
        log.info("deleting restaurantId with email {}", existingRestaurant.get().getEmail());
        restaurantRepository.delete(existingRestaurant.get());
        log.info("deleted restaurant successfully");
        return "deleted restaurant with restaurantId: "+restaurantId;
    }

    @Transactional
    public List<RestaurantResponse> getAllRestaurant() {
        
        log.info("fetching all the restaurants from database");
        List<RestaurantResponse> restaurantList =  new ArrayList<>();
        restaurantList.addAll(restaurantRepository.findAll()
                .stream()
                .map(restaurant-> mapper.restaurantToRestaurantResponse(restaurant))
                .toList());
        log.info("found total {} restaurants from the records",restaurantList.size());

        return restaurantList;


    }

    @Transactional
    public RestaurantResponse getRestaurant(String restaurantId) {
        
        log.info("fetching records for the restaurant with restaurantId {}", restaurantId);
        Optional<Restaurant> existingRestaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(!existingRestaurant.isPresent()){
            throw new RestaurantNotFoundException(new Throwable("Invalid restaurantId"));
        }
        log.info("found restaurant with email {} and restaurantId {}", existingRestaurant.get().getEmail(), restaurantId);
        RestaurantResponse response =  mapper.restaurantToRestaurantResponse(existingRestaurant.get());
        return response;

    }
}

