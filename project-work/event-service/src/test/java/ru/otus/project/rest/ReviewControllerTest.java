package ru.otus.project.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.entities.Review;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.ReviewNotFoundException;
import ru.otus.project.services.ReviewService;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера отзывов")
@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest {

    @MockitoBean
    ReviewService service;

    @Autowired
    MockMvc mvc;

    @DisplayName("Должен добавить отзыв")
    @Test
    void addReview() throws Exception {
        doReturn(review()).when(service).insert("comment", 1L);

        mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "comment": "comment",
                            "eventId": 1
                        }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если событие не найдено, при добавлении отзыва")
    @Test
    void addReview_EventNotFound() throws Exception {
        doThrow(EventNotFoundException.class).when(service).insert("comment", 1L);

        mvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "comment": "comment",
                            "eventId": 1
                        }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть список отзывов по идентификаторам событий")
    @Test
    void getReviews() throws Exception{
        doReturn(List.of(review())).when(service).findReviews(List.of(1L));

        mvc.perform(get("/api/events/reviews")
                .param("eventId", "1"))
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

    @DisplayName("Должен вернуть все события")
    @Test
    void testGetReviews() throws Exception {
        doReturn(List.of(review())).when(service).findAll();

        mvc.perform(get("/api/reviews"))
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

    @DisplayName("Должен вернуть отзыв по идентификатору")
    @Test
    void getReview() throws Exception {
        doReturn(review()).when(service).findById(1L);

        mvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если отзыв не найден, при поиске отзыва")
    @Test
    void getReview_ReviewNotFound() throws Exception {
        doThrow(ReviewNotFoundException.class).when(service).findById(1L);

        mvc.perform(get("/api/reviews/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен изменить отзыв")
    @Test
    void updateReview() throws Exception {
        doReturn(review()).when(service).update(1L, "comment");

        mvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "comment": "comment"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "comment": "comment",
                          "eventId": 1
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если отзыв не найден, при изменении отзыва")
    @Test
    void updateReview_ReviewNotFound() throws Exception {
        doThrow(ReviewNotFoundException.class).when(service).update(1L, "comment");

        mvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "comment": "comment"
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен удалить отзыв")
    @Test
    void deleteReview() throws Exception {
        mvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isOk());
    }

    private static Review review() {
        var review = new Review();
        review.setId(1L);
        review.setComment("comment");
        review.setEventId(1L);
        return review;
    }
}