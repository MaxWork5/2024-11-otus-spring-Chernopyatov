package ru.otus.hw.confiuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.StorageService;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {
    private final StorageService storageService;

    @Override
    public void run(String... args) {
        storageService.startsGenerateProducts();
        System.exit(130);
    }
}
