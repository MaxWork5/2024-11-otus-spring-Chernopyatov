package ru.otus.project.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.client.EventServiceClient;
import ru.otus.project.configuration.SecurityConfiguration;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.exceptions.NotFoundException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера пользователей")
@WebMvcTest(controllers = MemberController.class,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)})
class MemberControllerTest {

    @MockitoBean
    EventServiceClient client;

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен направить запрос на добавление события для пользователя")
    @Test
    void addEntry() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                "test",
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                        null,
                        "testEvent",
                        "testComment",
                        null,
                        null,
                        "2025-10-10",
                        "PRESENTATION",
                        null))))
                .when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/entries/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idEntry": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ENTRY_INSERT"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "test",
                          "events": [
                            {
                              "id": 1,
                              "name": "testEvent",
                              "comment": "testComment",
                              "date": "2025-10-10",
                              "type": "PRESENTATION"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 403 при запросе на добавление события для пользователя")
    @Test
    void addEntry_forbidden() throws Exception {
        mvc.perform(put("/api/entries/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idEntry": 1
                                }"""))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен вернуть ошибку с кодом 404 при запросе на добавление события для пользователя")
    @Test
    void addEntry_notFound() throws Exception {
        doThrow(NotFoundException.class)
                .when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/entries/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idEntry": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ENTRY_INSERT"))))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен направить запрос на получение списка событий, в которых принимает участие пользователь")
    @Test
    void getEntry_admin() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                        null,
                        "testEvent",
                        "testComment",
                        null,
                        null,
                        "2025-10-10",
                        "PRESENTATION",
                        null))))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/entries")
                        .queryParam("idMember", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("role", "ADMIN")
                                .claim("scope", "ENTRY_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "events": [
                            {
                              "id": 1,
                              "name": "testEvent",
                              "comment": "testComment",
                              "date": "2025-10-10",
                              "type": "PRESENTATION"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на получение списка событий, в которых принимает участие пользователь")
    @Test
    void getEntry_client() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                        null,
                        "testEvent",
                        "testComment",
                        null,
                        null,
                        "2025-10-10",
                        "PRESENTATION",
                        null))))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/entries")
                        .with(jwt().jwt(jwt -> jwt.claim("role", "CLIENT")
                                .claim("sub", "1")
                                .claim("scope", "ENTRY_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "events": [
                            {
                              "id": 1,
                              "name": "testEvent",
                              "comment": "testComment",
                              "date": "2025-10-10",
                              "type": "PRESENTATION"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен получить ошибку с кодом 400 при запросе на получение списка событий, в которых принимает участие пользователь")
    @Test
    void getEntry_badRequest() throws Exception {
        mvc.perform(get("/api/entries")
                        .with(jwt().jwt(jwt -> jwt.claim("role", "ADMIN")
                                .claim("scope", "ENTRY_GET"))))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Должен получить ошибку с кодом 404 при запросе на получение списка событий, в которых принимает участие пользователь")
    @Test
    void getEntry_notFound() throws Exception {
        doThrow(NotFoundException.class)
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/entries")
                        .queryParam("idMember", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("role", "ADMIN")
                                .claim("scope", "ENTRY_GET"))))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен получить ошибку с кодом 403 при запросе на получение списка событий, в которых принимает участие пользователь")
    @Test
    void getEntry_forbidden() throws Exception {
        mvc.perform(get("/api/entries")
                        .queryParam("idClient", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("role", "ORGANIZER")
                                .claim("scope", "MEMBER_GET"))))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен направить запрос на получение списка всех пользователей, участвующих в событии")
    @Test
    void getMemberOfEvent() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        "test",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                                null,
                                "testEvent",
                                "testComment",
                                null,
                                null,
                                "2025-10-10",
                                "PRESENTATION",
                                null)))),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/events/members")
                        .queryParam("idEvent", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "MEMBER_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items":
                            [
                              {
                              "id":1,
                              "login":"test",
                              "events":
                                [
                                  {
                                  "id":1,
                                  "name":"testEvent",
                                  "comment":"testComment",
                                  "date":"2025-10-10",
                                  "type":"PRESENTATION"
                                  }
                                ]
                              }
                            ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 403 при запросе на получение списка всех пользователей")
    @Test
    void getMemberOfEvent_forbidden() throws Exception {
        mvc.perform(get("/api/events/members")
                        .queryParam("idEvent", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_GET"))))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен направить запрос на получение всех пользователей")
    @Test
    void getMembers() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        "test",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                                null,
                                "testEvent",
                                "testComment",
                                null,
                                null,
                                "2025-10-10",
                                "PRESENTATION",
                                null)))),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/members/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items":
                            [
                              {
                              "id":1,
                              "login":"test",
                              "events":
                                [
                                  {
                                  "id":1,
                                  "name":"testEvent",
                                  "comment":"testComment",
                                  "date":"2025-10-10",
                                  "type":"PRESENTATION"
                                  }
                                ]
                              }
                            ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на получение пользователя по идентификатору")
    @Test
    void getMember() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                "test",
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                        null,
                        "testEvent",
                        "testComment",
                        null,
                        null,
                        "2025-10-10",
                        "PRESENTATION",
                        null))))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/members/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "login":"test",
                          "events":
                            [
                              {
                              "id":1,
                              "name":"testEvent",
                              "comment":"testComment",
                              "date":"2025-10-10",
                              "type":"PRESENTATION"
                              }
                            ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на добавление пользователя")
    @Test
    void addMember() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                "test",
                null,
                null,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendPostRequest(any(), any());

        mvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "test"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_INSERT"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "login":"test"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на удаление пользователя")
    @Test
    void deleteMember() throws Exception {
        mvc.perform(delete("/api/members/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_DELETE"))))
                .andExpect(status().isOk());
    }

    @DisplayName("Должен направить запрос на изменение пользователя")
    @Test
    void updateMember() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                "test",
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new ClientSendResponseDTO.ItemResponseDto(1L,
                        null,
                        "testEvent",
                        "testComment",
                        null,
                        null,
                        "2025-10-10",
                        "PRESENTATION",
                        null))))
                .when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testlogin",
                                    "idEvents": [1]
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "CLIENT_UPDATE"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "login":"test",
                          "events":
                            [
                              {
                              "id":1,
                              "name":"testEvent",
                              "comment":"testComment",
                              "date":"2025-10-10",
                              "type":"PRESENTATION"
                              }
                            ]
                        }""", JsonCompareMode.STRICT));
    }
}