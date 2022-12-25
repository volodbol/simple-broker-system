package com.task.broker.service;

import com.task.broker.dto.ApplicationUserCreationDto;
import com.task.broker.dto.BalanceTopUpDto;
import com.task.broker.model.ApplicationUser;
import com.task.broker.model.ApplicationUserRole;
import com.task.broker.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private static final int PASSWORD_MIN_LENGTH = 3;

    private final ApplicationUserRepository applicationUserRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<ApplicationUser> findUser(String login) {
        return applicationUserRepository.findByLogin(login);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ApplicationUser topUpUserBalance(BalanceTopUpDto balanceTopUpDto, ApplicationUser applicationUser) {
        BigDecimal currentBalance = applicationUser.getBalance();
        BigDecimal topUppedBalance = currentBalance.add(balanceTopUpDto.getTopUpValue());
        applicationUser.setBalance(topUppedBalance);
        return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser registerUser(ApplicationUserCreationDto userCreationDto) {
        isFieldsValid(userCreationDto);
        ApplicationUser applicationUser = ApplicationUser.builder()
                .login(userCreationDto.getLogin())
                .password(passwordEncoder.encode(userCreationDto.getPassword()))
                .applicationUserRole(ApplicationUserRole.USER)
                .balance(BigDecimal.ZERO)
                .build();
        return applicationUserRepository.save(applicationUser);
    }

    public void isLoginFree(String login) {
        if (applicationUserRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login - " + login + " is taken");
        }
    }

    private static void isPasswordValid(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be more than " + PASSWORD_MIN_LENGTH + " symbols");
        }
    }

    private void isFieldsValid(ApplicationUserCreationDto applicationUserCreationDto) {
        isLoginFree(applicationUserCreationDto.getLogin());
        isPasswordValid(applicationUserCreationDto.getPassword());
    }

}
