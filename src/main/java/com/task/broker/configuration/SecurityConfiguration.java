package com.task.broker.configuration;

import com.task.broker.model.ApplicationUserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/**").hasRole(ApplicationUserRole.ADMIN.name())
                .and()
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .formLogin().permitAll().defaultSuccessUrl("/orders", true)
                .and()
                .logout().permitAll();
        return http.build();
    }

}
