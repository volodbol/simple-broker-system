package com.task.broker.service;

import com.task.broker.dto.OrderDto;
import com.task.broker.mapper.OrderMapper;
import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import com.task.broker.model.OrderType;
import com.task.broker.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order saveOrder(OrderDto orderDto, ApplicationUser applicationUser) {
        Order order = orderMapper.toModel(orderDto);
        order.setApplicationUser(applicationUser);
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAllByApplicationUser(ApplicationUser applicationUser) {
        return orderRepository.findAllByApplicationUser(applicationUser);
    }

    public List<Order> findOrdersForAgreement(Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            return orderRepository.findAllMatchedOrders(
                    OrderType.SELL, order.getOrderInstrument(), order.getAmount(), order.getPrice());
        }
        return orderRepository.findAllMatchedOrders(
                OrderType.BUY, order.getOrderInstrument(), order.getAmount(), order.getPrice());
    }

    public OrderType getOppositeOrderType(Order order) {
        if (order.getOrderType() == OrderType.SELL) {
            return OrderType.BUY;
        }
        return OrderType.SELL;
    }

}
