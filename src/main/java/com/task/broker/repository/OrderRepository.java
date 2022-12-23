package com.task.broker.repository;

import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByApplicationUser(ApplicationUser applicationUser);

}
