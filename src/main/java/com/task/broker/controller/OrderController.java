package com.task.broker.controller;

import com.task.broker.dto.OrderDto;
import com.task.broker.model.ApplicationUser;
import com.task.broker.model.Order;
import com.task.broker.service.OrderInstrumentService;
import com.task.broker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderInstrumentService orderInstrumentService;

    @GetMapping
    public String allOrders(Model model, @AuthenticationPrincipal ApplicationUser applicationUser) {
        List<Order> orders = orderService.findAllByApplicationUser(applicationUser);
        model.addAttribute("orders", orders);
        return "user-orders";
    }

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        model.addAttribute("instruments", orderInstrumentService.findAll());
        model.addAttribute("orderDto", OrderDto.builder().build());
        return "create-order";
    }

    @PostMapping("/save")
    public String saveOrder(
            @ModelAttribute OrderDto orderDto, @AuthenticationPrincipal ApplicationUser applicationUser) {
        orderService.saveOrder(orderDto, applicationUser);
        return "redirect:/orders";
    }

}
