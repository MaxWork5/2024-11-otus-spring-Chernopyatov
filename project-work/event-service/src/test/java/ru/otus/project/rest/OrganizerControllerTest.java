package ru.otus.project.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.entities.Organizer;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.services.OrganizerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера организаторов")
@WebMvcTest(controllers = OrganizerController.class)
class OrganizerControllerTest {

    @MockitoBean
    OrganizerService service;

    @Autowired
    MockMvc mvc;

    private static Organizer organizer() {
        var organizer = new Organizer();
        organizer.setId(1L);
        organizer.setLogin("organizer");
        return organizer;
    }

    @DisplayName("Должен вернуть список организаторов")
    @Test
    void getOrganizers() throws Exception {
        doReturn(List.of(organizer())).when(service).findAll();

        mvc.perform(get("/api/organizers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "login":"organizer"
                            }
                          ]
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть организатора по идентификатору")
    @Test
    void getOrganizer() throws Exception {
        doReturn(organizer())
                .when(service).findById(any());

        mvc.perform(get("/api/organizers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "organizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если организатор не найден, при поиске организатора")
    @Test
    void getOrganizer_notFound() throws Exception {
        doThrow(OrganizerNotFoundException.class)
                .when(service).findById(any());

        mvc.perform(get("/api/organizers/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен создать организатора")
    @Test
    void createOrganizer() throws Exception {
        doReturn(organizer()).when(service).insert("organizer");

        mvc.perform(post("/api/organizers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                    "login": "organizer"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "organizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен изменить организатора")
    @Test
    void updateOrganizer() throws Exception {
        doReturn(organizer()).when(service).update(1L,"organizer");

        mvc.perform(put("/api/organizers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "organizer"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "login": "organizer"
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если организатор не найден, при изменении организатора")
    @Test
    void updateOrganizer_notFound() throws Exception {
        doThrow(OrganizerNotFoundException.class).when(service).update(1L,"organizer");

        mvc.perform(put("/api/organizers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "organizer"
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен удалить организатора")
    @Test
    void deleteOrganizer() throws Exception {
        mvc.perform(delete("/api/organizers/1"))
                .andExpect(status().isOk());
    }
}