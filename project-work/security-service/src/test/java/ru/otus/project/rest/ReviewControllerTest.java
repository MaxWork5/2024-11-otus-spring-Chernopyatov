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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера отзывов")
@WebMvcTest(controllers = ReviewController.class,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class)})
class ReviewControllerTest {

    @MockitoBean
    EventServiceClient client;

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен направить запрос на добавление отзыва")
    @Test
    void addReview() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                null,
                "comment",
                1L,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendPostRequest(any(), any());

        mvc.perform(post("/api/reviews")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comment": "comment",
                                  "eventId": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_INSERT"))))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 403 при запросе на добавление отзыва")
    @Test
    void addReview_forbidden() throws Exception {
        mvc.perform(post("/api/reviews")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "comment": "comment",
                                  "eventId": 1
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "ORGANIZER_DELETE"))))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Должен направить запрос на получение списка отзывов по идентификаторам событий")
    @Test
    void getReviews() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        null,
                        null,
                        "comment",
                        1L,
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

        mvc.perform(get("/api/events/reviews")
                        .queryParam("ids", "1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_GET"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items":
                            [
                              {
                              "id":1,
                              "comment":"comment",
                              "eventId":1
                              }
                            ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на получение всех событий")
    @Test
    void getAllReviews() throws Exception {
        doReturn(new ClientSendResponseDTO(
                List.of(new ClientSendResponseDTO.ItemResponseDto(
                        1L,
                        null,
                        null,
                        "comment",
                        1L,
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

        mvc.perform(get("/api/reviews/all")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_ALL"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "items": [
                            {
                              "id":1,
                              "comment":"comment",
                              "eventId":1
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на получение отзыва по идентификатору")
    @Test
    void getReview() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                null,
                "comment",
                1L,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendGetRequest(any(), any());

        mvc.perform(get("/api/reviews/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_ALL"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен направить запрос на изменение отзыва")
    @Test
    void updateReview() throws Exception {
        doReturn(new ClientSendResponseDTO(null,
                1L,
                null,
                "comment",
                1L,
                null,
                null,
                null,
                null,
                null))
                .when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/reviews/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "comment": "comment"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_UPDATE"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен отбросить ошибку 404 при изменении отзыва")
    @Test
    void updateReview_notFound() throws Exception {
        doThrow(NotFoundException.class).when(client).sendPutRequest(any(), any(), any());

        mvc.perform(put("/api/reviews/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "comment": "comment"
                                }""")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_UPDATE"))))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен направить запрос на удаление отзыва")
    @Test
    void deleteReview() throws Exception {
        mvc.perform(delete("/api/reviews/1")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "REVIEW_DELETE"))))
                .andExpect(status().isOk());
    }
}