package com.task.broker.service;

import com.task.broker.IntegrationTest;
import com.task.broker.configuration.PasswordEncoderConfiguration;
import com.task.broker.dto.ApplicationUserCreationDto;
import com.task.broker.model.ApplicationUser;
import com.task.broker.repository.ApplicationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@IntegrationTest
@Import(PasswordEncoderConfiguration.class)
class ApplicationUserDetailsServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private ApplicationUserService applicationUserService;

    private ApplicationUserDetailsService applicationUserDetailsService;

    @BeforeEach
    void beforeEach() {
        applicationUserService = new ApplicationUserService(applicationUserRepository, passwordEncoder);
        applicationUserDetailsService = new ApplicationUserDetailsService(applicationUserService);
    }

    @Test
    void testWhenUserRegisteredThenServiceMustReturnUser() {
        String login = "login";
        ApplicationUser applicationUser = applicationUserService
                .registerUser(new ApplicationUserCreationDto(login, "password"));
        assertDoesNotThrow(() -> {
            UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(login);
            assertEquals(applicationUser, userDetails, "Registered and found users must be equal!");
        }, "When user exist, then service must not throw any exceptions!");
    }

    @Test
    void testWhenUserNotRegisteredThenServiceMustThrowException() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> applicationUserDetailsService.loadUserByUsername("username"),
                "When user not registered, then service must throw exception!");
    }

}