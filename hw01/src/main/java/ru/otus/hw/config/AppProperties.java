package ru.otus.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class AppProperties implements TestFileNameProvider {
    private String testFileName;
}
