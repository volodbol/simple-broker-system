package com.task.broker.repository;

import com.task.broker.model.Order;
import com.task.broker.model.OrderAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAgreementRepository extends JpaRepository<OrderAgreement, Long> {

    List<OrderAgreement> findAllByMainOrder(Order order);

}
