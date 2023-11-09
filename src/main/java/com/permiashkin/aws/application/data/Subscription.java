package com.permiashkin.aws.application.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String subscriptionArn;
}
