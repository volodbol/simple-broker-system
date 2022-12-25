package com.task.broker.service;

import com.task.broker.IntegrationTest;
import com.task.broker.configuration.PasswordEncoderConfiguration;
import com.task.broker.dto.ApplicationUserCreationDto;
import com.task.broker.dto.BalanceTopUpDto;
import com.task.broker.model.ApplicationUser;
import com.task.broker.repository.ApplicationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@IntegrationTest
@Import(PasswordEncoderConfiguration.class)
class ApplicationUserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private ApplicationUserService applicationUserService;

    @BeforeEach
    void beforeEach() {
        applicationUserService = new ApplicationUserService(applicationUserRepository, passwordEncoder);
    }

    @ParameterizedTest
    @MethodSource("validRegisterData")
    void testWhenRegisterDataValidThenUserRegisteredAndHasId(String login, String password) {
        applicationUserService.registerUser(new ApplicationUserCreationDto(login, password));
        Optional<ApplicationUser> userOptional = applicationUserService.findUser(login);
        assertFalse(userOptional.isEmpty(), "Registered user can not be null!");
        ApplicationUser applicationUser = userOptional.get();
        assertTrue(applicationUser.getId() > 0, "User id can not be less than zero!");
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterData")
    void testWhenRegisterDataInvalidThenThrowsException(String login, String password) {
        ApplicationUserCreationDto userCreationDto = new ApplicationUserCreationDto(login, password);
        assertThrows(
                IllegalArgumentException.class,
                () -> applicationUserService.registerUser(userCreationDto),
                "With invalid data service must throw exception!"
        );
    }

    @Test
    void testWhenLoginTakenThenThrowsException() {
        ApplicationUserCreationDto userCreationDto = new ApplicationUserCreationDto("login1", "password");
        applicationUserService.registerUser(userCreationDto);
        assertThrows(
                IllegalArgumentException.class,
                () -> applicationUserService.registerUser(userCreationDto),
                "If login taken, service must throw exception!"
        );
    }

    @Test
    void testWhenUserRegisteredThenItCanBeFoundByLogin() {
        String login = "login";
        applicationUserService.registerUser(new ApplicationUserCreationDto(login, "password"));
        Optional<ApplicationUser> userOptional = applicationUserService.findUser(login);
        assertFalse(userOptional.isEmpty(), "Registered user can not be null!");
    }

    @Test
    void testWhenUserTopUpBalanceThenItMustChange() {
        ApplicationUser applicationUser = applicationUserService.registerUser(
                new ApplicationUserCreationDto("login1", "password"));
        BigDecimal balanceBeforeTopUp = applicationUser.getBalance();
        BigDecimal topUpValue = BigDecimal.valueOf(235.43);
        BalanceTopUpDto balanceTopUpDto = BalanceTopUpDto.builder()
                .currentBalance(balanceBeforeTopUp)
                .topUpValue(topUpValue)
                .build();

        ApplicationUser applicationUser1 = applicationUserService.topUpUserBalance(balanceTopUpDto, applicationUser);

        BigDecimal actual = applicationUser1.getBalance();
        assertNotEquals(balanceBeforeTopUp, actual, "After top-up balance must change!");
        BigDecimal expected = balanceBeforeTopUp.add(topUpValue);
        assertEquals(expected, actual, "Balance must be top-upped!");
    }

    static Stream<Arguments> invalidRegisterData() {
        return Stream.of(
                Arguments.of("user", "dsg"),
                Arguments.of("error", ""),
                Arguments.of("hello", "\n"),
                Arguments.of("invalid", "541"),
                Arguments.of("data", "%$#"),
                Arguments.of("fdsmsdk", "*8H")
        );
    }

    static Stream<Arguments> validRegisterData() {
        return Stream.of(
                Arguments.of("valid", "bdhs84nf"),
                Arguments.of("user1", "user1"),
                Arguments.of("username", "dgffd454"),
                Arguments.of("login", "password"),
                Arguments.of("somedata", "random"),
                Arguments.of("hello", "world")
        );
    }

}