package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.client.EventServiceClient;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.dto.requests.client.ClientInsertRequestDTO;
import ru.otus.project.dto.requests.client.ClientUpdateRequestDTO;
import ru.otus.project.dto.requests.client.EntryRequestDTO;
import ru.otus.project.model.Role;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final EventServiceClient client;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на запись клиента на событие")
    @PreAuthorize("hasAuthority('SCOPE_ENTRY_INSERT')")
    @PutMapping("/api/entries/{id}")
    public ClientSendResponseDTO addEntry(@PathVariable Long id,
                                          @Validated @RequestBody EntryRequestDTO dto) {
        return client.sendPutRequest("/api/entries/{id}", id, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос получение событие пользователя")
    @PreAuthorize("hasAuthority('SCOPE_ENTRY_GET')")
    @GetMapping("/api/entries")
    public ClientSendResponseDTO getEntry(@RequestParam(required = false) Long idClient,
                                          JwtAuthenticationToken token) {
        var jwt = token.getToken();

        if (Role.CLIENT.name().equals(jwt.getClaim("role")) && jwt.getSubject() != null) {
            return client.sendGetRequest("/api/entries/{id}", jwt.getSubject());
        } else if (Role.ADMIN.name().equals(jwt.getClaim("role")) && idClient != null) {
            return client.sendGetRequest("/api/entries/{id}", idClient.toString());
        } else {
            throw new IllegalArgumentException("Invalid Request");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на поиск участников события")
    @PreAuthorize("hasAuthority('SCOPE_MEMBER_GET')")
    @GetMapping("/api/members")
    public ClientSendResponseDTO getMember(@RequestParam Long idEvent) {
        return client.sendGetRequest("/api/members/{id}", idEvent.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос получение всех пользователей")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_GET')")
    @GetMapping("/api/clients/all")
    public ClientSendResponseDTO getClients() {
        return client.sendGetRequest("/api/clients", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос получение пользователя по идентификатору")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_GET')")
    @GetMapping("/api/clients/{idClient}")
    public ClientSendResponseDTO getClient(@PathVariable Long idClient) {
        return client.sendGetRequest("/api/clients/{id}", idClient.toString());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Запрос на добавление пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_INSERT')")
    @PostMapping("/api/clients")
    public ClientSendResponseDTO addClient(@Validated @RequestBody ClientInsertRequestDTO dto) {
        return client.sendPostRequest("/api/clients", dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на удаление пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_DELETE')")
    @DeleteMapping("/api/clients/{idClient}")
    public void deleteClient(@PathVariable Long idClient) {
        client.sendDeleteRequest("/api/clients/{id}", idClient.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на изменение пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_UPDATE')")
    @PutMapping("/api/clients/{id}")
    public ClientSendResponseDTO updateClient(@PathVariable Long id,
                                              @Validated @RequestBody ClientUpdateRequestDTO dto) {
        return client.sendPutRequest("/api/clients/{id}", id, dto);
    }
}
