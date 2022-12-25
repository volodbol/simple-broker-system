package com.task.broker.listener;

import com.task.broker.IntegrationTest;
import com.task.broker.event.OrderSessionChangedEvent;
import com.task.broker.model.Order;
import com.task.broker.model.OrderInstrument;
import com.task.broker.model.OrderType;
import com.task.broker.service.OrderAgreementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.verify;

@SpringBootTest
@IntegrationTest
class OrderSessionChangedEventListenerTest {

    @MockBean
    private OrderAgreementService orderAgreementService;

    @Autowired
    private OrderSessionChangedEventListener orderSessionChangedEventListener;

    private final Clock clock = Clock.fixed(Instant.parse("2022-12-24T10:15:30.00Z"), ZoneId.systemDefault());

    @Test
    void testWhenEventWithOpenedSessionAppearsThenListenerCallsCreateAgreements() {
        OrderInstrument orderInstrument = OrderInstrument.builder()
                .id(1L)
                .instrumentName("Oil")
                .build();
        Order order = Order.builder()
                .id(1L)
                .orderType(OrderType.SELL)
                .orderInstrument(orderInstrument)
                .isSessionActive(true)
                .createdAt(LocalDateTime.now(clock))
                .build();
        OrderSessionChangedEvent event = new OrderSessionChangedEvent(order);

        orderSessionChangedEventListener.onApplicationEvent(event);

        verify(orderAgreementService).createMatchedAgreements(order);
    }

    @Test
    void testWhenEventWithClosedSessionAppearsThenListenerCallsCloseNotPerformedAgreements() {
        OrderInstrument orderInstrument = OrderInstrument.builder()
                .id(1L)
                .instrumentName("Oil")
                .build();
        Order order = Order.builder()
                .id(1L)
                .orderType(OrderType.SELL)
                .orderInstrument(orderInstrument)
                .isSessionActive(false)
                .createdAt(LocalDateTime.now(clock))
                .build();
        OrderSessionChangedEvent event = new OrderSessionChangedEvent(order);

        orderSessionChangedEventListener.onApplicationEvent(event);

        verify(orderAgreementService).closeNotPerformedAgreements(order);
    }

}