package com.task.broker.controller;

import com.task.broker.dto.BalanceTopUpDto;
import com.task.broker.mapper.BalanceTopUpMapper;
import com.task.broker.model.ApplicationUser;
import com.task.broker.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final BalanceTopUpMapper balanceTopUpMapper;

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public String getCurrentProfile(Model model, @AuthenticationPrincipal ApplicationUser applicationUser) {
        model.addAttribute("user", applicationUser);
        return "user-profile";
    }

    @GetMapping("/top-up")
    public String getTopUpForm(Model model, @AuthenticationPrincipal ApplicationUser applicationUser) {
        model.addAttribute("balanceTopUpDto", balanceTopUpMapper.toDto(applicationUser));
        return "user-top-up";
    }

    @PostMapping("/top-up")
    public String topUpUserBalance(
            @ModelAttribute BalanceTopUpDto balanceTopUpDto,
            @AuthenticationPrincipal ApplicationUser applicationUser) {
        applicationUserService.topUpUserBalance(balanceTopUpDto, applicationUser);
        return "redirect:/profile";
    }


}
