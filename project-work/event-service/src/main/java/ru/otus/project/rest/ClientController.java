package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.dto.request.client.ClientInsertRequestDTO;
import ru.otus.project.dto.request.client.ClientUpdateRequestDTO;
import ru.otus.project.dto.request.client.EntryRequestDTO;
import ru.otus.project.dto.response.ClientResponseDTO;
import ru.otus.project.dto.response.CollectionWrapperDTO;
import ru.otus.project.dto.response.EventResponseDTO;
import ru.otus.project.services.ClientService;


@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @Operation(summary = "Запрос на запись пользователя на событие")
    @PutMapping("/api/entries/{id}")
    public ResponseEntity<ClientResponseDTO> addEntry(@PathVariable Long id, @RequestBody EntryRequestDTO dto) {
        var response = ClientResponseDTO.fromClient(service.signUp(id, dto.idEntry()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на получение событий пользователя")
    @GetMapping("/api/entries/{id}")
    public ResponseEntity<CollectionWrapperDTO> getEntry(@PathVariable Long id) {
        var response = service.lookOnEvents(id).stream()
                .map(EventResponseDTO::fromEvent)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на участников события")
    @GetMapping("/api/members/{id}")
    public ResponseEntity<CollectionWrapperDTO> getMember(@PathVariable Long id) {
        var response = service.findMembersOfEvent(id).stream()
                .map(ClientResponseDTO::fromClient)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение пользователей")
    @GetMapping("/api/clients")
    public ResponseEntity<CollectionWrapperDTO> getClients() {
        var response = service.findAll().stream()
                .map(ClientResponseDTO::fromClient)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на поиск пользователя по идентификатора")
    @GetMapping("/api/clients/{id}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable Long id) {
        var response = ClientResponseDTO.fromClient(service.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на создание пользователя")
    @PostMapping("/api/clients")
    public ResponseEntity<ClientResponseDTO> addClient(@RequestBody ClientInsertRequestDTO dto) {
        var response = ClientResponseDTO.fromClient(service.insert(dto.login()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на удаления пользователя")
    @DeleteMapping("/api/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.deleteClient(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Запрос на изменение пользователя")
    @PutMapping("/api/clients/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id,
                                                          @RequestBody ClientUpdateRequestDTO dto) {
        var response = ClientResponseDTO.fromClient(service.updateClient(id, dto.login(), dto.idEvents()));

        return ResponseEntity.ok(response);
    }
}
