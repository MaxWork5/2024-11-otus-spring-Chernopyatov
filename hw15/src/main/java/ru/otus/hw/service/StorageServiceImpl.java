package ru.otus.hw.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.Wood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final FactoryGateway factory;

    @Override
    public void startsGenerateProducts() {
        for (int i = 0; i < 10; i++) {
            var resources = new ArrayList<Wood>();
            for (int j = 0; j < RandomUtils.secure().randomInt(1, 20); j++) {
                var wood = new Wood(RandomUtils.secure().randomLong(1, 6500));
                resources.add(wood);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            log.info("Generating products in {} woods:", resources.size());
            resources.forEach(wood -> log.info("{} kilo of wood.", wood.weight()));
            var products = shrink(factory.process(resources));
            log.info("Generated products from woods:");
            products.forEach(product -> log.info("{} in quantity of {} pieces", product.name(), product.quantity()));
        }
    }

    private List<Product> shrink(Collection<Product> products) {
        var distinctProducts = new ArrayList<Product>();
        var list = products.stream().map(Product::name).distinct().toList();
        for (var name : list) {
            var counter = 0L;
            for (var product : products) {
                if (product.name().equals(name)) {
                    counter = counter + product.quantity();
                }
            }
            distinctProducts.add(new Product(name, counter));
        }
        return distinctProducts;
    }
}
