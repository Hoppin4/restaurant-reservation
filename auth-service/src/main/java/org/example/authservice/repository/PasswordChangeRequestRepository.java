package org.example.authservice.repository;

import org.example.authservice.entity.PasswordChangeRequest;
import org.example.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordChangeRequestRepository extends JpaRepository<PasswordChangeRequest, UUID> {
    Optional<PasswordChangeRequest> findByToken(String token);

    Optional<PasswordChangeRequest> findByUser(User user);

    void deleteByUser(User user);
}
