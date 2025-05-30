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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера организаторов")
@WebMvcTest(controllers = OrganizerController.class,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)})
class OrganizerControllerTest {

    @MockitoBean
    EventServiceClient client;

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен направить запрос на получение списка организаторов")
    @Test
    void getOrganizers() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        "testorganizer",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)).when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/organizers/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items": [
                            {
                              "id":1,
                              "login":"testorganizer"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 403 при запросе на получение списка организаторов")
    @Test
    void getOrganizers_forbidden() throws Exception {
        mvc.perform(get("/api/organizers/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "badScope"))))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен направить запрос на получение организатора по идентификатору")
    @Test
    void getOrganizer() throws Exception {
        doReturn(new ClientSendResponseDTO(
                null,
                1L,
                "testorganizer",
                null,
                null,
                null,
                null,
                null,
                null,
                null)).when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/organizers/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "testorganizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на создание организатора")
    @Test
    void addOrganizer() throws Exception {
        doReturn(new ClientSendResponseDTO(
                null,
                1L,
                "testorganizer",
                null,
                null,
                null,
                null,
                null,
                null,
                null)).when(client).sendPostRequest(any(), any());

        mvc.perform(post("/api/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "testorganizer"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_INSERT"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "testorganizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на обновление организатора")
    @Test
    void updateOrganizer() throws Exception {
        doReturn(new ClientSendResponseDTO(
                null,
                1L,
                "testorganizer",
                null,
                null,
                null,
                null,
                null,
                null,
                null)).when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/organizers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "testorganizer"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_UPDATE"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "testorganizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен отбросить ошибку 404 при обновлении организатора")
    @Test
    void updateOrganizer_notFound() throws Exception {
        doThrow(NotFoundException.class).when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/organizers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "testorganizer"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_UPDATE"))))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен направить запрос на удаление организатора")
    @Test
    void deleteOrganizer() throws Exception {
        mvc.perform(delete("/api/organizers/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_DELETE"))))
                .andExpect(status().isOk());
    }
}