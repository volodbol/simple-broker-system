package com.task.broker.dto;

import com.task.broker.model.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private OrderType orderType;

    private String orderInstrument;

    private BigDecimal amount;

    private BigDecimal price;

}
