package com.task.broker.service;

import com.task.broker.model.Order;
import com.task.broker.model.OrderAgreement;
import com.task.broker.repository.OrderAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAgreementService {

    private final OrderAgreementRepository orderAgreementRepository;

    private final OrderService orderService;

    public List<OrderAgreement> findAllByMainOrder(Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find order with id - " + id));
        return orderAgreementRepository.findAllByMainOrder(order);
    }

}
