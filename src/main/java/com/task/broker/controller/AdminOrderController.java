package com.task.broker.controller;

import com.task.broker.model.Order;
import com.task.broker.model.OrderAgreement;
import com.task.broker.publisher.OrderSessionChangedEventPublisher;
import com.task.broker.service.OrderAgreementService;
import com.task.broker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private static final int PAGE_SIZE = 10;

    private final OrderService orderService;

    private final OrderAgreementService orderAgreementService;

    private final OrderSessionChangedEventPublisher sessionChangedEventPublisher;

    @GetMapping("/orders")
    public String getAllOrders(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Page<Order> orders = orderService.findAll(Pageable.ofSize(PAGE_SIZE).withPage(page));
        model.addAttribute("pageNumber", page);
        model.addAttribute("pages", orders.getTotalPages());
        model.addAttribute("orders", orders);
        return "admin-all-orders";
    }

    @GetMapping("/orders/{id}/agreements")
    public String getOrderAgreements(@PathVariable(name = "id") Long orderId, Model model) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find order with id - " + orderId));
        model.addAttribute("orderType", orderService.getOppositeOrderType(order));
        model.addAttribute("orderId", orderId);
        model.addAttribute("agreements", orderAgreementService.findAllByOrder(order));
        return "admin-order-agreements";
    }

    @PostMapping("/orders/{orderId}/agreements/{agreementId}")
    public String confirmAgreement(@PathVariable Long orderId, @PathVariable Long agreementId, Model model) {
        OrderAgreement orderAgreement = orderAgreementService.findById(agreementId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find agreement with id - " + agreementId));
        orderAgreement.setIsPerformed(true);
        orderAgreementService.updateOrderAgreement(orderAgreement);
        return String.format("redirect:/admin/orders/%s/agreements", orderId);
    }

    @PostMapping("/orders/{id}/session")
    public String changeOrderSession(@PathVariable Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find order with id - " + id));
        Boolean changedSessionState = !order.getIsSessionActive();
        order.setIsSessionActive(changedSessionState);
        orderService.updateOrder(order);
        sessionChangedEventPublisher.publishOrderSessionChangedEvent(order);
        return "redirect:/admin/orders";
    }

}
