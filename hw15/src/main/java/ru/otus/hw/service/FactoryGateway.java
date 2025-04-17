package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.Wood;

import java.util.Collection;

@MessagingGateway
public interface FactoryGateway {

    @Gateway(requestChannel = "woodChannel", replyChannel = "productChannel")
    Collection<Product> process(Collection<Wood> resource);
}
