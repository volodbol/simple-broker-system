package com.task.broker.repository;

import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import com.task.broker.model.OrderInstrument;
import com.task.broker.model.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByApplicationUser(ApplicationUser applicationUser);

    @Query("select o from Order o " +
            "where o.isSessionActive = true and o.orderType = ?1 and o.orderInstrument = ?2" +
            " and o.amount = ?3 and o.price = ?4")
    List<Order> findAllMatchedOrders(OrderType orderType, OrderInstrument orderInstrument,
                                     BigDecimal amount, BigDecimal price);

}
