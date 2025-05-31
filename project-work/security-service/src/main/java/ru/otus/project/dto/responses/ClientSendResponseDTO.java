package ru.otus.project.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "ДТО ответа")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientSendResponseDTO(@Schema(description = "Список сущностей")
                                    List<ItemResponseDto> items,
                                    @Schema(description = "Идентификатор")
                                    Long id,
                                    @Schema(description = "Логин")
                                    String login,
                                    @Schema(description = "Комментарий")
                                    String comment,
                                    @Schema(description = "Идентификатор события")
                                    Long eventId,
                                    @Schema(description = "Название события") @NotNull
                                    String name,
                                    @Schema(description = "Описание")
                                    String description,
                                    @Schema(description = "Дата") @NotNull
                                    LocalDate date,
                                    @Schema(description = "Тип события") @NotNull
                                    String type,
                                    @Schema(description = "Список событий")
                                    List<ItemResponseDto> events) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Сущность ответа")
    public record ItemResponseDto(@Schema(description = "Идентификатор")
                                  Long id,
                                  @Schema(description = "Логин")
                                  String login,
                                  @Schema(description = "Имя")
                                  String name,
                                  @Schema(description = "Комментарий")
                                  String comment,
                                  @Schema(description = "Идентификатор события")
                                  Long eventId,
                                  @Schema(description = "Описание")
                                  String description,
                                  @Schema(description = "Дата")
                                  String date,
                                  @Schema(description = "Тип события")
                                  String type,
                                  @Schema(description = "Список событий")
                                  List<ItemResponseDto> events) {
    }
}
