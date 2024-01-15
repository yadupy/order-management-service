package com.accenture.pip.ordermanagementservice.repository;

import com.accenture.pip.ordermanagementservice.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    //@Query("Select fi from FoodItem where fi.foodItemId")
    List<FoodItem> findAllByFoodItemIdIn(List<String> foodItemId);
}
