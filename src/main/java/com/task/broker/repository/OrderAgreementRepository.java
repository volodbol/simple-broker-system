package com.task.broker.repository;

import com.task.broker.model.Order;
import com.task.broker.model.OrderAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAgreementRepository extends JpaRepository<OrderAgreement, Long> {

    @Query("select o from OrderAgreement o where o.firstOrder = ?1 or o.secondOrder = ?1")
    List<OrderAgreement> findAllByOrder(Order order);


    @Query("select o from OrderAgreement o where o.isPerformed = false and o.firstOrder = ?1 or o.secondOrder = ?1")
    List<OrderAgreement> findAllByOrderAndIsPerformedFalse(Order order);

}
