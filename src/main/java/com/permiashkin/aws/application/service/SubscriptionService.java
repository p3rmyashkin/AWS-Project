package com.permiashkin.aws.application.service;


import com.permiashkin.aws.application.data.Subscription;
import com.permiashkin.aws.application.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public void save(String email, String subscriptionArn) {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionArn(subscriptionArn);
        subscription.setEmail(email);
        repository.save(subscription);
    }

    public String findSubscriptionArnByEmail(String email) {
        return repository.findByEmail(email)
                .map(Subscription::getSubscriptionArn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }
}
