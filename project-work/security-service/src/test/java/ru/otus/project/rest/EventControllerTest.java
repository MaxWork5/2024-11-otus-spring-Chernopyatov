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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера событий")
@WebMvcTest(controllers = EventController.class,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)})
class EventControllerTest {

    @MockitoBean
    EventServiceClient client;

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен направить запрос на получение списка событий, на которые можно зарегистрироваться")
    @Test
    void getEventsForSignUp() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        null,
                        "testevent",
                        null,
                        null,
                        "This is a test",
                        "2025-01-01",
                        "Корпоративное мероприятие",
                        null)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )).when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/events/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items": [
                            {
                              "id":1,
                              "name":"testevent",
                              "description":"This is a test",
                              "date":"2025-01-01",
                              "type":"Корпоративное мероприятие"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен получить ошибку с кодом 403 при запросе на получение списка событий")
    @Test
    void getEventsFromToday_forbidden() throws Exception {
        mvc.perform(get("/api/events/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_INSERT"))))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен направить запрос на получение событий по фильтрам")
    @Test
    void getEvents() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        null,
                        "testevent",
                        null,
                        null,
                        "This is a test",
                        "2025-01-01",
                        "Корпоративное мероприятие",
                        null)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )).when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/events")
                        .queryParam("startDate", "2025-01-01")
                        .queryParam("endDate", "2025-01-01")
                        .queryParam("type", "CORPORATE")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items": [
                            {
                              "id":1,
                              "name":"testevent",
                              "description":"This is a test",
                              "date":"2025-01-01",
                              "type":"Корпоративное мероприятие"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на создание события")
    @Test
    void addEvent() throws Exception {
        doReturn(new ClientSendResponseDTO(
                null,
                1L,
                null,
                null,
                null,
                "testevent",
                "This is a test",
                LocalDate.of(2025, 1, 1),
                "Корпоративное мероприятие",
                null
        )).when(client).sendPostRequest(any(), any());

        mvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "testevent",
                                  "description": "This is a test",
                                  "date": "2025-01-01",
                                  "type": "CORPORATE",
                                  "organizerId": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_INSERT"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "name":"testevent",
                          "description":"This is a test",
                          "date":"2025-01-01",
                          "type":"Корпоративное мероприятие"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на обновление события")
    @Test
    void updateEvent() throws Exception {
        doReturn(new ClientSendResponseDTO(
                null,
                1L,
                null,
                null,
                null,
                "testevent",
                "This is a test",
                LocalDate.of(2025, 1, 1),
                "Корпоративное мероприятие",
                null
        )).when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "testevent",
                                  "description": "This is a test",
                                  "date": "2025-01-01",
                                  "type": "CORPORATE",
                                  "organizerId": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_UPDATE"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "name":"testevent",
                          "description":"This is a test",
                          "date":"2025-01-01",
                          "type":"Корпоративное мероприятие"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен отбрасывать ошибку 404 при обновлении события")
    @Test
    void updateEvent_notFound() throws Exception {
        doThrow(NotFoundException.class).when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "testevent",
                                  "description": "This is a test",
                                  "date": "2025-01-01",
                                  "type": "CORPORATE",
                                  "organizerId": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_UPDATE"))))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен направить запрос на получение всех событий")
    @Test
    void getAllEvents() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        null,
                        "testevent",
                        null,
                        null,
                        "This is a test",
                        "2025-01-01",
                        "Корпоративное мероприятие",
                        null)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        )).when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/admins/events")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_ALL"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items": [
                            {
                              "id":1,
                              "name":"testevent",
                              "description":"This is a test",
                              "date":"2025-01-01",
                              "type":"Корпоративное мероприятие"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на удаление события")
    @Test
    void deleteEvent() throws Exception {
        mvc.perform(delete("/api/admins/events/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "EVENT_DELETE"))))
                .andExpect(status().isOk());
    }
}