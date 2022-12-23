package com.task.broker.service;

import com.task.broker.model.ApplicationUser;
import com.task.broker.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    public Optional<ApplicationUser> findUser(String login) {
        return applicationUserRepository.findByLogin(login);
    }

}
