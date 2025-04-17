package ru.otus.hw.service;

import ru.otus.hw.domain.Product;
import ru.otus.hw.domain.SawDust;

public interface ChemicalShopService {
    Product makeProduct(SawDust resource);
}
