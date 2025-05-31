package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.dto.request.event.EventInsertRequestDTO;
import ru.otus.project.dto.request.event.EventQueryDTO;
import ru.otus.project.dto.request.event.EventUpdateRequestDTO;
import ru.otus.project.dto.response.CollectionWrapperDTO;
import ru.otus.project.dto.response.EventResponseDTO;
import ru.otus.project.entities.UpdateEvent;
import ru.otus.project.services.EventService;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @Operation(summary = "Запрос на получение событий с сегодняшнего дня")
    @GetMapping("/api/events/all")
    public ResponseEntity<CollectionWrapperDTO> getEvents() {
        var response = service.findEventsForSignUp().stream()
                .map(EventResponseDTO::fromEvent)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение событий по условиям")
    @GetMapping("/api/events")
    public ResponseEntity<CollectionWrapperDTO> getEvents(EventQueryDTO dto) {
        var response = service.findByDatesAndType(dto.startDate(), dto.endDate(), dto.type()).stream()
                .map(EventResponseDTO::fromEvent)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на создание события")
    @PostMapping("/api/events")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventInsertRequestDTO dto) {
        var response = EventResponseDTO
                .fromEvent(service.insert(dto.name(), dto.description(), dto.date(), dto.type(), dto.organizerId()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на изменение события")
    @PutMapping("/api/events/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @RequestBody EventUpdateRequestDTO dto) {
        var response = EventResponseDTO.fromEvent(service.update(UpdateEvent.fromDto(id, dto)));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на получение всех событий")
    @GetMapping("/api/admins/events")
    public ResponseEntity<CollectionWrapperDTO> getAdminEvents() {
        var response = service.findAll().stream()
                .map(EventResponseDTO::fromEvent)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на удаление события")
    @DeleteMapping("/api/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok().build();
    }
}
