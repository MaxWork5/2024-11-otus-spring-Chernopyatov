package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Коллекция ДТО")
public record CollectionWrapperDTO(@Schema(description = "Список ДТО") List<?> items) {
}
