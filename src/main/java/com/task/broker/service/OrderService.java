package com.task.broker.service;

import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import com.task.broker.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order saveOrder(Order order, ApplicationUser applicationUser) {
        order.setApplicationUser(applicationUser);
        return orderRepository.save(order);
    }

    public List<Order> findAllByApplicationUser(ApplicationUser applicationUser) {
        return orderRepository.findAllByApplicationUser(applicationUser);
    }

}
