package com.task.broker.event;

import com.task.broker.model.Order;
import org.springframework.context.ApplicationEvent;

public class OrderSessionChangedEvent extends ApplicationEvent {

    public OrderSessionChangedEvent(Order order) {
        super(order);
    }

}
