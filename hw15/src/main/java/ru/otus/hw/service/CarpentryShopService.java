package ru.otus.hw.service;

import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.Wood;

public interface CarpentryShopService {
    Product makeProduct(Wood resource);
}
