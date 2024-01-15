package com.accenture.pip.ordermanagementservice.entity;


import com.accenture.pip.ordermanagementservice.dto.FoodItemDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "ORDER_ITEM")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderItem {

    @Column(name = "ORDER_ITEM_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ORDER_ITEM_UUID")
    @UuidGenerator
    private String orderItemId;

    @Column(name = "FOOD_ITEM_ID")
    private String foodItemId;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="ORDER_ID", nullable=false)
    private Order order;
}
