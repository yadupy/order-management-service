package com.accenture.pip.ordermanagementservice.mapper;

import com.accenture.pip.ordermanagementservice.dto.OrderDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderItemDTO;
import com.accenture.pip.ordermanagementservice.dto.OrderResponseDTO;
import com.accenture.pip.ordermanagementservice.dto.RestaurantResponse;
import com.accenture.pip.ordermanagementservice.entity.Order;
import com.accenture.pip.ordermanagementservice.entity.OrderItem;
import com.accenture.pip.ordermanagementservice.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper( OrderMapper.class );


    @Mapping(target = "id", source = "orderId")
    OrderResponseDTO orderToOrderResponseDTO(Order entity);

    Order orderDTOToOrder(OrderDTO orderDTO);

    OrderItem orderItemDTOToOrderItem(OrderItemDTO itemDTO);
}
