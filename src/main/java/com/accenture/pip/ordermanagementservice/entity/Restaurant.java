package com.accenture.pip.ordermanagementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "RESTAURANT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant implements Serializable {

    @Column(name = "RESTAURANT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "RESTAURANT_UUID")
    @UuidGenerator
    private String restaurantId;

    @Column(name = "NAME")
    private String restaurantName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "AREA_SERVES")
    private List<Integer> areaServes;

    @OneToMany(mappedBy = "restaurant",fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItem> foodItemSet = new ArrayList<>();

    @Column(name = "CREATED_AT")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
}

