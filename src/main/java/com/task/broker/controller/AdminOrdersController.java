package com.task.broker.controller;

import com.task.broker.model.Order;
import com.task.broker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminOrdersController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin-all-orders";
    }

    @PostMapping("/orders/{id}/session")
    public String changeOrderSession(@PathVariable Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find order with id - " + id));
        Boolean changedSessionState = !order.getIsSessionActive();
        order.setIsSessionActive(changedSessionState);
        orderService.updateOrder(order);
        return "redirect:/admin/orders";
    }

}
