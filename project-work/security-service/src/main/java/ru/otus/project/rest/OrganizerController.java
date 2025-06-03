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
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.dto.requests.organizer.OrganizerInsertRequestDTO;
import ru.otus.project.dto.requests.organizer.OrganizerUpdateRequestDTO;

@RestController
@RequiredArgsConstructor
public class OrganizerController {

    private final EventServiceClient client;

    @Operation(summary = "Запрос на получение всех организаторов")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER_GET')")
    @GetMapping("/api/organizers/all")
    public ClientSendResponseDTO getOrganizers() {
        return client.sendGetRequest("/api/organizers", null);
    }

    @Operation(summary = "Запрос на получение организатора")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER_GET')")
    @GetMapping("/api/organizers/{idOrganizer}")
    public ClientSendResponseDTO getOrganizer(@PathVariable Long idOrganizer) {
        return client.sendGetRequest("/api/organizers/{id}", idOrganizer.toString());
    }

    @Operation(summary = "Запрос на создание организатора")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER_INSERT')")
    @PostMapping("/api/organizers")
    public ClientSendResponseDTO addOrganizer(@Validated @RequestBody OrganizerInsertRequestDTO dto) {
        return client.sendPostRequest("/api/organizers", dto);
    }

    @Operation(summary = "Запрос на изменение организатора")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER_UPDATE')")
    @PutMapping("/api/organizers/{id}")
    public ClientSendResponseDTO updateOrganizer(@PathVariable Long id,
                                                 @Validated @RequestBody OrganizerUpdateRequestDTO dto) {
        return client.sendPutRequest("/api/organizers/{id}", id, dto);
    }

    @Operation(summary = "Запрос на удаления организатора")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER_DELETE')")
    @DeleteMapping("/api/organizers/{idOrganizer}")
    public void deleteOrganizer(@PathVariable Long idOrganizer) {
        client.sendDeleteRequest("/api/organizers/{id}", idOrganizer.toString());
    }
}
