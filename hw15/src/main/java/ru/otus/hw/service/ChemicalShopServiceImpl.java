package ru.otus.hw.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.SawDust;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChemicalShopServiceImpl implements ChemicalShopService {
    private static final Map<String, Long> PRODUCTS_COST = Map.of("Chipboard", 25L,
            "Fibreboard", 6L,
            "Plywood", 18L,
            "OSB", 12L,
            "Sawdust Briquette", 1L,
            "CBPB",14L);

    @Override
    public Product makeProduct(SawDust resource) {
        var productName = new ArrayList<>(PRODUCTS_COST.keySet());
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
