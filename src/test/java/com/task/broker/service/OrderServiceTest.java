package com.task.broker.service;

import com.task.broker.IntegrationTest;
import com.task.broker.dto.OrderDto;
import com.task.broker.mapper.OrderMapper;
import com.task.broker.model.*;
import com.task.broker.repository.ApplicationUserRepository;
import com.task.broker.repository.OrderInstrumentRepository;
import com.task.broker.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@IntegrationTest
class OrderServiceTest {

    private static final String INSTRUMENT_NAME = "Oil";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private OrderInstrumentRepository orderInstrumentRepository;

    private OrderService orderService;

    @BeforeEach
    void beforeEach() {
        OrderMapper orderMapper = new OrderMapper(new OrderInstrumentService(orderInstrumentRepository));
        orderService = new OrderService(orderRepository, orderMapper);
        saveInstrument();
    }

    @Test
    void testWhenOrderSavedThenItHasId() {
        ApplicationUser applicationUser = createNewUser("login");
        OrderDto orderDto = OrderDto.builder()
                .orderType(OrderType.SELL)
                .orderInstrument(INSTRUMENT_NAME)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .build();
        Order order = orderService.saveOrder(orderDto, applicationUser);
        assertNotNull(order, "Created order can not be null!");
        assertTrue(order.getId() > 0, "Created order must have id more than zero!");
    }

    @Test
    void testWhenOrdersSavedThenServiceReturnThemAllByUser() {
        ApplicationUser applicationUser = createNewUser("username");
        int ordersAmount = 15;
        for (int i = 0; i < ordersAmount; i++) {
            OrderDto orderDto = OrderDto.builder()
                    .orderType(OrderType.SELL)
                    .orderInstrument(INSTRUMENT_NAME)
                    .amount(BigDecimal.valueOf(i))
                    .price(BigDecimal.ONE)
                    .build();
            orderService.saveOrder(orderDto, applicationUser);
        }
        Page<Order> orders = orderService.findAllByApplicationUser(applicationUser, Pageable.unpaged());
        assertFalse(orders.isEmpty(), "Service can not return empty page after saving");
        assertEquals(orders.getSize(), ordersAmount, "Service must return exact amount of orders");
    }

    @Test
    void testWhenOrdersSavedThenServiceReturnThemAll() {
        ApplicationUser applicationUser = createNewUser("newuser");
        int ordersAmount = 15;
        for (int i = 0; i < ordersAmount; i++) {
            OrderDto orderDto = OrderDto.builder()
                    .orderType(OrderType.SELL)
                    .orderInstrument(INSTRUMENT_NAME)
                    .amount(BigDecimal.valueOf(i))
                    .price(BigDecimal.ONE)
                    .build();
            orderService.saveOrder(orderDto, applicationUser);
        }
        Page<Order> orders = orderService.findAll(Pageable.unpaged());
        assertFalse(orders.isEmpty(), "Service can not return empty page after saving");
        assertEquals(orders.getSize(), ordersAmount, "Service must return exact amount of orders");
    }

    @Test
    void testWhenOrderUpdatedThenServiceMustReturnUpdatedVersion() {
        ApplicationUser applicationUser = createNewUser("user1");
        OrderDto orderDto = OrderDto.builder()
                .orderType(OrderType.SELL)
                .orderInstrument(INSTRUMENT_NAME)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .build();
        Order order = orderService.saveOrder(orderDto, applicationUser);
        assertFalse(order.getIsSessionActive(), "After creating, order isSessionActive must be false");

        order.setIsSessionActive(true);
        Order actual = orderService.updateOrder(order);
        assertTrue(actual.getIsSessionActive(), "After updating, order isSessionActive must be true");
        assertEquals(order.getId(), actual.getId(), "Order id must not be changed!");

        Optional<Order> expectedOptional = orderService.findById(actual.getId());
        assertFalse(expectedOptional.isEmpty(), "Found order can not be null");
        Order expected = expectedOptional.get();

        assertEquals(expected, actual, "Returned and updated orders must be equal");
    }

    @Test
    void testWhenOrderSavedThenServiceMustFindItById() {
        ApplicationUser applicationUser = createNewUser("user1");
        OrderDto orderDto = OrderDto.builder()
                .orderType(OrderType.SELL)
                .orderInstrument(INSTRUMENT_NAME)
                .amount(BigDecimal.ONE)
                .price(BigDecimal.ONE)
                .build();
        Order actual = orderService.saveOrder(orderDto, applicationUser);

        Optional<Order> expectedOptional = orderService.findById(actual.getId());
        assertFalse(expectedOptional.isEmpty(), "Found order can not be null");
        Order expected = expectedOptional.get();

        assertEquals(expected, actual, "Service must return exact order after saving");
    }

    @ParameterizedTest
    @MethodSource("orderTypeValues")
    void testWhenPassedOrderTypeThenServiceMustReturnOppositeValue(OrderType orderType, OrderType expected) {
        Order order = Order.builder()
                .orderType(orderType)
                .build();
        OrderType actual = orderService.getOppositeOrderType(order);
        assertEquals(actual, expected, "Service must return opposite order type!");
    }

    @ParameterizedTest
    @EnumSource(OrderType.class)
    void testWhenOrdersForAgreementExistThenServiceMustReturnThemAll(OrderType orderType) {
        ApplicationUser applicationUser = createNewUser("user");
        OrderDto orderDto = OrderDto.builder()
                .orderType(orderType)
                .orderInstrument(INSTRUMENT_NAME)
                .amount(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .build();
        Order order = orderService.saveOrder(orderDto, applicationUser);
        order.setIsSessionActive(true);
        orderService.updateOrder(order);

        int amount = 17;
        createOrdersForAgreement(amount, order);

        List<Order> ordersForAgreement = orderService.findOrdersForAgreement(order);
        assertFalse(ordersForAgreement.isEmpty(), "Service can not return empty list after saving");
        assertEquals(
                ordersForAgreement.size(), amount, "Service must return exact amount of orders for agreement");
    }

    private void createOrdersForAgreement(int amount, Order order) {
        ApplicationUser applicationUser = createNewUser("login");
        OrderType oppositeOrderType = orderService.getOppositeOrderType(order);
        for (int i = 0; i < amount; i++) {
            OrderDto orderDto = OrderDto.builder()
                    .orderType(oppositeOrderType)
                    .orderInstrument(order.getOrderInstrument().getInstrumentName())
                    .amount(order.getAmount())
                    .price(order.getPrice())
                    .build();
            Order savedOrder = orderService.saveOrder(orderDto, applicationUser);
            savedOrder.setIsSessionActive(true);
            orderService.updateOrder(savedOrder);

        }
    }

    private static Stream<Arguments> orderTypeValues() {
        return Stream.of(
                Arguments.of(OrderType.SELL, OrderType.BUY),
                Arguments.of(OrderType.BUY, OrderType.SELL)
        );
    }

    private ApplicationUser createNewUser(String login) {
        return applicationUserRepository.save(
                ApplicationUser.builder()
                        .login(login)
                        .password("pass")
                        .balance(BigDecimal.ZERO)
                        .applicationUserRole(ApplicationUserRole.USER)
                        .build()
        );
    }

    private void saveInstrument() {
        orderInstrumentRepository.save(OrderInstrument.builder().instrumentName(INSTRUMENT_NAME).build());
    }

}