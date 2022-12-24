package com.task.broker.publisher;

import com.task.broker.event.OrderSessionChangedEvent;
import com.task.broker.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSessionChangedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishOrderSessionChangedEvent(Order order) {
        OrderSessionChangedEvent event = new OrderSessionChangedEvent(order);
        applicationEventPublisher.publishEvent(event);
    }

}
