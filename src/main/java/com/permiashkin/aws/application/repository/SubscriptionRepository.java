package com.permiashkin.aws.application.repository;

import com.permiashkin.aws.application.data.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByEmail(String email);

    void deleteByEmail(String email);
}
