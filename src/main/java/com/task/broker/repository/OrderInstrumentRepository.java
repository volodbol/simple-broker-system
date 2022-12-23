package com.task.broker.repository;

import com.task.broker.model.OrderInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderInstrumentRepository extends JpaRepository<OrderInstrument, UUID> {
}
