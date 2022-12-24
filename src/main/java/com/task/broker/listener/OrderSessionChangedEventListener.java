package com.task.broker.listener;

import com.task.broker.event.OrderSessionChangedEvent;
import com.task.broker.model.Order;
import com.task.broker.service.OrderAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSessionChangedEventListener implements ApplicationListener<OrderSessionChangedEvent> {

    private final OrderAgreementService orderAgreementService;

    @Override
    public void onApplicationEvent(OrderSessionChangedEvent event) {
        Order order = (Order) event.getSource();
        if (Boolean.TRUE.equals(order.getIsSessionActive())) {
            orderAgreementService.createMatchedAgreements(order);
        } else {
            orderAgreementService.closeNotPerformedAgreements(order);
        }
    }

}
