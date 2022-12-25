package com.task.broker.service;

import com.task.broker.dto.BalanceTopUpDto;
import com.task.broker.model.ApplicationUser;
import com.task.broker.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

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

}
