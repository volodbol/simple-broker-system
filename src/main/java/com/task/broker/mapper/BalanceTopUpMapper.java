package com.task.broker.mapper;

import com.task.broker.dto.BalanceTopUpDto;
import com.task.broker.model.ApplicationUser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceTopUpMapper {

    public BalanceTopUpDto toDto(ApplicationUser applicationUser) {
        return BalanceTopUpDto.builder()
                .currentBalance(applicationUser.getBalance())
                .topUpValue(BigDecimal.ZERO)
                .build();
    }

}
