package com.task.broker.service;

import com.task.broker.model.Order;
import com.task.broker.model.OrderAgreement;
import com.task.broker.repository.OrderAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderAgreementService {

    private final OrderAgreementRepository orderAgreementRepository;

    private final OrderService orderService;

    public Optional<OrderAgreement> findById(Long id) {
        return orderAgreementRepository.findById(id);
    }

    public List<OrderAgreement> findAllByOrder(Order order) {
        return orderAgreementRepository.findAllByOrder(order);
    }

    public OrderAgreement updateOrderAgreement(OrderAgreement orderAgreement) {
        return orderAgreementRepository.save(orderAgreement);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void createMatchedAgreements(Order order) {
        List<OrderAgreement> orderAgreements = orderService.findOrdersForAgreement(order).stream()
                .map(secondOrder -> OrderAgreement.builder()
                        .firstOrder(order)
                        .secondOrder(secondOrder)
                        .isPerformed(false)
                        .isCancelled(false)
                        .build())
                .collect(Collectors.toList());
        orderAgreementRepository.saveAll(orderAgreements);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void closeNotPerformedAgreements(Order order) {
        List<OrderAgreement> notPerformedOrderAgreements = orderAgreementRepository
                .findAllByOrderAndIsPerformedFalse(order);
        notPerformedOrderAgreements.forEach(orderAgreement -> orderAgreement.setIsCancelled(true));
        orderAgreementRepository.saveAll(notPerformedOrderAgreements);
    }

}
