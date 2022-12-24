package com.task.broker.repository;

import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByApplicationUser(ApplicationUser applicationUser);

}
