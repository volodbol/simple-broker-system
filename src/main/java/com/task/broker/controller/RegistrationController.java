package com.task.broker.controller;

import com.task.broker.dto.ApplicationUserCreationDto;
import com.task.broker.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public String getRegistrationPage(Model model) {
        model.addAttribute("userCreationDto", new ApplicationUserCreationDto());
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute ApplicationUserCreationDto userCreationDto, Model model) {
        try {
            applicationUserService.registerUser(userCreationDto);
        } catch (IllegalArgumentException e) {
            log.error("Can not register new user, error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return getRegistrationPage(model);
        }
        return "redirect:/login";
    }

}
