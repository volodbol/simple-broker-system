package com.task.broker.service;

import com.task.broker.model.OrderInstrument;
import com.task.broker.repository.OrderInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderInstrumentService {

    private final OrderInstrumentRepository orderInstrumentRepository;

    public List<OrderInstrument> findAll() {
        return orderInstrumentRepository.findAll();
    }

    public Optional<OrderInstrument> findByName(String name) {
        return orderInstrumentRepository.findByInstrumentName(name);
    }

}
