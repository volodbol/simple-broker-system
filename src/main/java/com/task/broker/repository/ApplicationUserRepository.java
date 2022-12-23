package com.task.broker.repository;

import com.task.broker.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByLogin(String login);

}
