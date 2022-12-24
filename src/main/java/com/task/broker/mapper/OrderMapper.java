package com.task.broker.mapper;

import com.task.broker.dto.OrderDto;
import com.task.broker.model.Order;
import com.task.broker.model.OrderInstrument;
import com.task.broker.service.OrderInstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderInstrumentService orderInstrumentService;

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderInstrument(order.getOrderInstrument().getInstrumentName())
                .orderType(order.getOrderType())
                .amount(order.getAmount())
                .price(order.getPrice())
                .build();
    }

    public Order toModel(OrderDto orderDto) {
        OrderInstrument orderInstrument = orderInstrumentService.findByName(orderDto.getOrderInstrument())
                .orElseThrow(
                        () -> new IllegalArgumentException("Can't find instrument - " + orderDto.getOrderInstrument()));
        return Order.builder()
                .orderInstrument(orderInstrument)
                .orderType(orderDto.getOrderType())
                .amount(orderDto.getAmount())
                .price(orderDto.getPrice())
                .isSessionActive(false)
                .build();
    }

}
