package com.task.broker.service;

import com.task.broker.IntegrationTest;
import com.task.broker.model.OrderInstrument;
import com.task.broker.repository.OrderInstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@IntegrationTest
class OrderInstrumentServiceTest {

    @Autowired
    private OrderInstrumentRepository orderInstrumentRepository;

    private OrderInstrumentService orderInstrumentService;

    @BeforeEach
    void beforeAll() {
        orderInstrumentService = new OrderInstrumentService(orderInstrumentRepository);
    }

    @Test
    void testWhenInstrumentSavedThenServiceReturnItByName() {
        String orderInstrumentName = "Oil";
        OrderInstrument actual = orderInstrumentRepository
                .save(OrderInstrument.builder().instrumentName(orderInstrumentName).build());
        assertTrue(actual.getId() > 0, "Saved instrument can not have it less than zero");

        Optional<OrderInstrument> optionalExpected = orderInstrumentService.findByName(orderInstrumentName);
        assertFalse(optionalExpected.isEmpty(), "Saved instrument can not be null");
        OrderInstrument expected = optionalExpected.get();

        assertEquals(expected, actual, "Instruments must be equal");
    }

    @Test
    void testWhenInstrumentsSavedThanServiceMustReturnThemAll() {
        int amount = 13;
        saveInstruments(amount);

        List<OrderInstrument> orderInstruments = orderInstrumentService.findAll();
        assertFalse(orderInstruments.isEmpty(), "Service must return saved instruments!");
        assertEquals(orderInstruments.size(), amount, "Service must return exact amount of instruments");
    }

    private void saveInstruments(int amount) {
        for (int i = 0; i < amount; i++) {
            orderInstrumentRepository.save(OrderInstrument.builder().instrumentName("Name " + i).build());
        }
    }

}