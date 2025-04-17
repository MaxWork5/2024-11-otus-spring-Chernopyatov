package ru.otus.hw.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.Wood;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarpentryShopServiceImpl implements CarpentryShopService {
    private static final Map<String, Long> PRODUCTS_COST = Map.of("Table", 49L,
            "Stool", 16L,
            "Wardrobe", 150L,
            "Door", 45L,
            "Cabinet", 75L,
            "Chair", 32L,
            "Bench", 40L);

    @Override
    public Product makeProduct(Wood resource) {
        var productName = PRODUCTS_COST.keySet().stream().toList();
        var list = PRODUCTS_COST.entrySet().stream()
                .filter(product -> product.getValue() <= resource.weight())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Product product = null;
        while (product == null) {
            var name = productName.get(RandomUtils.secure().randomInt(0, productName.size()));
            if (list.containsKey(name)) {
                product = new Product(name, resource.weight() / list.get(name));
            }
        }
        log.info("\n Making product: {}, \n in quantity: {}", product.name(), product.quantity());
        return product;
    }
}
