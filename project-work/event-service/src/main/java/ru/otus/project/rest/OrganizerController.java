package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.dto.request.organizer.OrganizerInsertRequestDTO;
import ru.otus.project.dto.request.organizer.OrganizerUpdateRequestDTO;
import ru.otus.project.dto.response.CollectionWrapperDTO;
import ru.otus.project.dto.response.OrganizerResponseDTO;
import ru.otus.project.services.OrganizerService;

@RestController
@RequiredArgsConstructor
public class OrganizerController {
    private final OrganizerService service;

    @Operation(summary = "Запрос на получение всех организаторов")
    @GetMapping("/api/organizers")
    public ResponseEntity<CollectionWrapperDTO> getOrganizers() {
        var response = service.findAll().stream()
                .map(OrganizerResponseDTO::fromOrganizer)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение организатора")
    @GetMapping("/api/organizers/{id}")
    public ResponseEntity<OrganizerResponseDTO> getOrganizer(@PathVariable Long id) {
        var response = OrganizerResponseDTO.fromOrganizer(service.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на создание организатора")
    @PostMapping("/api/organizers")
    public ResponseEntity<OrganizerResponseDTO> createOrganizer(@RequestBody OrganizerInsertRequestDTO dto) {
        var response = OrganizerResponseDTO.fromOrganizer(service.insert(dto.login()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на изменение организатора")
    @PutMapping("/api/organizers/{id}")
    public ResponseEntity<OrganizerResponseDTO> updateOrganizer(@PathVariable Long id,
                                                                @RequestBody OrganizerUpdateRequestDTO dto) {
        var response = OrganizerResponseDTO.fromOrganizer(service.update(id, dto.login()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на удаления организатора")
    @DeleteMapping("/api/organizers/{id}")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok().build();
    }
}
