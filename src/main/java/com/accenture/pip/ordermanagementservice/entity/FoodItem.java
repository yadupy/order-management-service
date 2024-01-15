package com.accenture.pip.ordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "FOOD_ITEM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem implements Serializable {

    @Column(name = "FOOD_ITEM_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FOOD_ITEM_UUID")
    @UuidGenerator
    private String foodItemId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String Description;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="RESTAURANT_ID", nullable=false)
    private Restaurant restaurant;

}
