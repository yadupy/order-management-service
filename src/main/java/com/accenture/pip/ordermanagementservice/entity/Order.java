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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Column(name = "ORDER_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ORDER_UUID")
    @UuidGenerator
    private String orderId;

    @Column(name = "STATUS")
    private String status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(name = "ADDRESS_ID")
    private String addressId;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "RESTAURANT_ID")
    private String restaurantId;

    @Column(name = "TOTAL")
    private Double total = 0.0d;

    @Column(name = "CREATED_AT")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;


}
