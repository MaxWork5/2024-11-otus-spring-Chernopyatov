package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.client.EventServiceClient;
import ru.otus.project.dto.requests.event.EventInsertRequestDTO;
import ru.otus.project.dto.requests.event.EventQueryDTO;
import ru.otus.project.dto.requests.event.EventUpdateRequestDTO;
import ru.otus.project.dto.responses.ClientSendResponseDTO;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventServiceClient client;

    @Operation(summary = "Запрос на получение событий с сегодняшнего дня")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_GET')")
    @GetMapping("/api/events/all")
    public ClientSendResponseDTO getEventsForSignUp() {
        return client.sendGetRequest("/api/events/all", null);
    }

    @Operation(summary = "Запрос на получение событий по условиям")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_GET')")
    @GetMapping("/api/events")
    public ClientSendResponseDTO getEvents(@Validated EventQueryDTO dto) {
        return client.sendGetRequest(String.format("/api/events?startDate=%s&endDate=%s&type=%s",
                            dto.startDate(), dto.endDate(), dto.type()),
                    null);
    }

    @Operation(summary = "Запрос на создание события")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_INSERT')")
    @PostMapping("/api/events")
    public ClientSendResponseDTO addEvent(@Validated @RequestBody EventInsertRequestDTO dto) {
        return client.sendPostRequest("/api/events", dto);
    }

    @Operation(summary = "Запрос на изменение события")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_UPDATE')")
    @PutMapping("/api/events/{id}")
    public ClientSendResponseDTO updateEvent(@PathVariable Long id,
                                             @Validated @RequestBody EventUpdateRequestDTO dto) {
        return client.sendPutRequest("/api/events/{id}", id, dto);
    }

    @Operation(summary = "Запрос на получение всех событий")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_ALL')")
    @GetMapping("/api/admins/events")
    public ClientSendResponseDTO getAllEvents() {
        return client.sendGetRequest("/api/admins/events", null);
    }

    @Operation(summary = "Запрос на удаление события")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_EVENT_DELETE')")
    @DeleteMapping("/api/admins/events/{idEvent}")
    public void deleteEvent(@PathVariable Long idEvent) {
        client.sendDeleteRequest("/api/events/{id}", idEvent.toString());
    }
}
