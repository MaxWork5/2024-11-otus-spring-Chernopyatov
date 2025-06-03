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
import ru.otus.project.dto.request.member.MemberInsertRequestDTO;
import ru.otus.project.dto.request.member.MemberUpdateRequestDTO;
import ru.otus.project.dto.request.member.EntryRequestDTO;
import ru.otus.project.dto.response.MemberResponseDTO;
import ru.otus.project.dto.response.CollectionWrapperDTO;
import ru.otus.project.dto.response.EventResponseDTO;
import ru.otus.project.services.MemberService;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;

    @Operation(summary = "Запрос на запись пользователя на событие")
    @PutMapping("/api/entries/{id}")
    public ResponseEntity<MemberResponseDTO> addEntry(@PathVariable Long id, @RequestBody EntryRequestDTO dto) {
        var response = MemberResponseDTO.fromClient(service.signUp(id, dto.idEntry()));

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
    @GetMapping("/api/events/members/{id}")
    public ResponseEntity<CollectionWrapperDTO> getMemberOfEvent(@PathVariable Long id) {
        var response = service.findMembersOfEvent(id).stream()
                .map(MemberResponseDTO::fromClient)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение пользователей")
    @GetMapping("/api/members")
    public ResponseEntity<CollectionWrapperDTO> getMembers() {
        var response = service.findAll().stream()
                .map(MemberResponseDTO::fromClient)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на поиск пользователя по идентификатора")
    @GetMapping("/api/members/{id}")
    public ResponseEntity<MemberResponseDTO> getMember(@PathVariable Long id) {
        var response = MemberResponseDTO.fromClient(service.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на создание пользователя")
    @PostMapping("/api/members")
    public ResponseEntity<MemberResponseDTO> addMember(@RequestBody MemberInsertRequestDTO dto) {
        var response = MemberResponseDTO.fromClient(service.insert(dto.login()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на удаления пользователя")
    @DeleteMapping("/api/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        service.deleteMember(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Запрос на изменение пользователя")
    @PutMapping("/api/members/{id}")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable Long id,
                                                          @RequestBody MemberUpdateRequestDTO dto) {
        var response = MemberResponseDTO.fromClient(service.updateMember(id, dto.login(), dto.idEvents()));

        return ResponseEntity.ok(response);
    }
}
