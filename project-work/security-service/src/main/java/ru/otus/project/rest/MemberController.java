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
import ru.otus.project.dto.requests.member.MemberInsertRequestDTO;
import ru.otus.project.dto.requests.member.MemberUpdateRequestDTO;
import ru.otus.project.dto.requests.member.EntryRequestDTO;
import ru.otus.project.model.Role;

@RestController
@RequiredArgsConstructor
public class MemberController {

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
    public ClientSendResponseDTO getEntry(@RequestParam(required = false) Long idMember,
                                          JwtAuthenticationToken token) {
        var jwt = token.getToken();

        if (Role.CLIENT.name().equals(jwt.getClaim("role")) && jwt.getSubject() != null) {
            return client.sendGetRequest("/api/entries/{id}", jwt.getSubject());
        } else if (Role.ADMIN.name().equals(jwt.getClaim("role")) && idMember != null) {
            return client.sendGetRequest("/api/entries/{id}", idMember.toString());
        } else {
            throw new IllegalArgumentException("Invalid Request");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на поиск участников события")
    @PreAuthorize("hasAuthority('SCOPE_MEMBER_GET')")
    @GetMapping("/api/events/members")
    public ClientSendResponseDTO getMemberOfEvent(@RequestParam Long idEvent) {
        return client.sendGetRequest("/api/events/members/{id}", idEvent.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос получение всех пользователей")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_GET')")
    @GetMapping("/api/members/all")
    public ClientSendResponseDTO getMembers() {
        return client.sendGetRequest("/api/members", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос получение пользователя по идентификатору")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_GET')")
    @GetMapping("/api/members/{idMember}")
    public ClientSendResponseDTO getMember(@PathVariable Long idMember) {
        return client.sendGetRequest("/api/members/{id}", idMember.toString());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Запрос на добавление пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_INSERT')")
    @PostMapping("/api/members")
    public ClientSendResponseDTO addMember(@Validated @RequestBody MemberInsertRequestDTO dto) {
        return client.sendPostRequest("/api/members", dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на удаление пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_DELETE')")
    @DeleteMapping("/api/members/{idMember}")
    public void deleteMember(@PathVariable Long idMember) {
        client.sendDeleteRequest("/api/members/{id}", idMember.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на изменение пользователя")
    @PreAuthorize("hasAuthority('SCOPE_CLIENT_UPDATE')")
    @PutMapping("/api/members/{id}")
    public ClientSendResponseDTO updateMember(@PathVariable Long id,
                                              @Validated @RequestBody MemberUpdateRequestDTO dto) {
        return client.sendPutRequest("/api/members/{id}", id, dto);
    }
}
