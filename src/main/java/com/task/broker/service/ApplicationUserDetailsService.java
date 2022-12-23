package com.task.broker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserService applicationUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserService.findUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can not find user with login " + username));
    }

}
