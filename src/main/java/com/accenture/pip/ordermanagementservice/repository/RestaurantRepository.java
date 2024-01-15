package com.accenture.pip.ordermanagementservice.repository;

import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmailOrContactNumber(String email, String contactNumber);

    Optional<Restaurant> findByRestaurantId(String customerId);

}
