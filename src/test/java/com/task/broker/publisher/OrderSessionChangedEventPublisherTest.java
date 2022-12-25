package com.task.broker.publisher;

import com.task.broker.IntegrationTest;
import com.task.broker.event.OrderSessionChangedEvent;
import com.task.broker.model.Order;
import com.task.broker.model.OrderInstrument;
import com.task.broker.model.OrderType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@IntegrationTest
class OrderSessionChangedEventPublisherTest {

    @Autowired
    private OrderSessionChangedEventPublisher orderSessionChangedEventPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final Clock clock = Clock.fixed(Instant.parse("2022-12-24T10:15:30.00Z"), ZoneId.systemDefault());

    @Test
    void testWhenEventPublishedThenPublisherCalled() {
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

        orderSessionChangedEventPublisher.publishOrderSessionChangedEvent(order);

        verify(applicationEventPublisher).publishEvent(any(OrderSessionChangedEvent.class));
    }

    @TestConfiguration
    static class EventPublisherConfiguration {

        @Bean
        @Primary
        ApplicationEventPublisher applicationEventPublisher() {
            return mock(ApplicationEventPublisher.class);
        }

    }

}