package com.task.broker.service;

import com.task.broker.IntegrationTest;
import com.task.broker.dto.OrderDto;
import com.task.broker.mapper.OrderMapper;
import com.task.broker.model.*;
import com.task.broker.repository.ApplicationUserRepository;
import com.task.broker.repository.OrderAgreementRepository;
import com.task.broker.repository.OrderInstrumentRepository;
import com.task.broker.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@IntegrationTest
class OrderAgreementServiceTest {

    private static final String INSTRUMENT_NAME = "Oil";

    @Autowired
    private OrderInstrumentRepository orderInstrumentRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private OrderAgreementRepository orderAgreementRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderService orderService;

    private OrderAgreementService orderAgreementService;

    @BeforeEach
    void beforeEach() {
        saveInstrument();
        OrderInstrumentService orderInstrumentService = new OrderInstrumentService(orderInstrumentRepository);
        OrderMapper orderMapper = new OrderMapper(orderInstrumentService);
        orderService = new OrderService(orderRepository, orderMapper);
        orderAgreementService = new OrderAgreementService(orderAgreementRepository, orderService);
    }

    @Test
    void testWhenOrderAgreementExistThenServiceMustReturnById() {
        ApplicationUser user1 = createNewUser("user1");
        ApplicationUser user2 = createNewUser("user2");
        OrderAgreement actual = orderAgreementRepository.save(
                OrderAgreement.builder()
                        .firstOrder(createOrder(OrderType.SELL, user1))
                        .secondOrder(createOrder(OrderType.BUY, user2))
                        .isPerformed(false)
                        .isCancelled(false)
                        .build()
        );

        assertTrue(actual.getId() > 0, "Created agreement must have id more than zero!");

        Optional<OrderAgreement> expectedOptional = orderAgreementService.findById(actual.getId());
        assertFalse(expectedOptional.isEmpty(), "Service can not return empty for saved agreement!");
        OrderAgreement expected = expectedOptional.get();

        assertEquals(expected, actual, "Saved and retrieved agreements must be equal");
    }

    @Test
    void testWhenOrderAgreementExistThenServiceMustReturnByOrder() {
        ApplicationUser user1 = createNewUser("user1");
        ApplicationUser user2 = createNewUser("user2");
        Order firstOrder = createOrder(OrderType.SELL, user1);
        Order secondOrder = createOrder(OrderType.BUY, user2);
        OrderAgreement actual = orderAgreementRepository.save(
                OrderAgreement.builder()
                        .firstOrder(firstOrder)
                        .secondOrder(secondOrder)
                        .isPerformed(false)
                        .isCancelled(false)
                        .build()
        );

        assertTrue(actual.getId() > 0, "Created agreement must have id more than zero!");

        List<OrderAgreement> orderAgreements = orderAgreementService.findAllByOrder(firstOrder);
        assertFalse(orderAgreements.isEmpty(), "Service can not return empty list for saved agreement!");

        assertEquals(orderAgreements.get(0), actual, "Saved and retrieved agreements must be equal");
    }

    @Test
    void testWhenOrderAgreementUpdatedThenServiceMustReturnIt() {
        ApplicationUser user1 = createNewUser("user1");
        ApplicationUser user2 = createNewUser("user2");
        OrderAgreement expected = orderAgreementRepository.save(
                OrderAgreement.builder()
                        .firstOrder(createOrder(OrderType.SELL, user1))
                        .secondOrder(createOrder(OrderType.BUY, user2))
                        .isPerformed(false)
                        .isCancelled(false)
                        .build()
        );

        expected.setIsPerformed(true);
        OrderAgreement actual = orderAgreementService.updateOrderAgreement(expected);

        assertEquals(expected, actual, "Updated and retrieved agreements must be equal");
    }

    @Test
    void testWhenOrdersForAgreementExistThenServiceMustCreateAndReturnIt() {
        ApplicationUser applicationUser = createNewUser("username");
        Order order = createOrder(OrderType.SELL, applicationUser);
        int amount = 4;
        createOrdersForAgreement(amount, order);

        orderAgreementService.createMatchedAgreements(order);

        List<OrderAgreement> orderAgreements = orderAgreementService.findAllByOrder(order);
        assertFalse(orderAgreements.isEmpty(), "Service must create agreements between matched orders");
        assertEquals(
                orderAgreements.size(),
                amount,
                "Service must create exact value of agreements between matched orders"
        );
    }

    @Test
    void testWhenNotPerformedAgreementsClosedThenServiceMustReturnClosedAgreements() {
        ApplicationUser applicationUser = createNewUser("username");
        Order order = createOrder(OrderType.SELL, applicationUser);
        int amount = 4;
        createOrdersForAgreement(amount, order);

        orderAgreementService.createMatchedAgreements(order);
        orderAgreementService.closeNotPerformedAgreements(order);

        List<OrderAgreement> orderAgreements = orderAgreementService.findAllByOrder(order);
        assertFalse(orderAgreements.isEmpty(), "Service must create agreements between matched orders");
        assertEquals(
                orderAgreements.size(),
                amount,
                "Service must create exact value of agreements between matched orders"
        );
        long closedAmount = orderAgreements.stream().filter(OrderAgreement::getIsCancelled).count();
        assertEquals(
                amount,
                closedAmount,
                "Service must close exact value of agreements between matched orders"
        );
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

    private Order createOrder(OrderType orderType, ApplicationUser applicationUser) {
        OrderDto orderDto = OrderDto.builder()
                .orderType(orderType)
                .orderInstrument(INSTRUMENT_NAME)
                .amount(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .build();
        return orderService.saveOrder(orderDto, applicationUser);
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