package com.accenture.pip.ordermanagementservice.repository;

import com.accenture.pip.ordermanagementservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);
    List<Order> findAllByCustomerId(String customerId);
    List<Order> findAllByRestaurantId(String restaurantId);

   //@Query(value = "Select o.* from orders o Inner Join order_item oi on o.order_id = oi.order_id Inner join food_item f on oi.food_item_id = f.food_item_id where f.food_item_uuid=:foodItemId",nativeQuery = true)
   @Query(value = "Select o.* from orders o Inner Join order_item oi on o.order_id = oi.order_id Inner join food_item f on oi.food_item_id = f.food_item_uuid where f.food_item_uuid=:foodItemId",nativeQuery = true)
    List<Order> getAllByFoodItem(String foodItemId);
}
