package ru.otus.project.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.services.EventService;

import java.time.LocalDate;
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

@DisplayName("Тест контроллера событий")
@WebMvcTest(controllers = EventController.class)
class EventControllerTest {

    @MockitoBean
    EventService service;

    @Autowired
    MockMvc mvc;

    private static Event event() {
        var event = new Event();
        event.setId(1L);
        event.setDate(LocalDate.of(2025, 1, 1));
        event.setDescription("This is a test");
        event.setName("testevent");
        event.setType(EventType.CORPORATE);
        event.setOrganizerId(1L);
        return event;
    }

    @DisplayName("Должен вернуть список событий с сегодняшнего дня")
    @Test
    void getEventsForSignUp() throws Exception {
        doReturn(List.of(event())).when(service).findEventsForSignUp();

        mvc.perform(get("/api/events/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "name":"testevent",
                            "description":"This is a test",
                            "date":"2025-01-01",
                            "type":"Корпоративное мероприятие"
                            }
                          ]
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть события определённого типа, находящихся во определённом временном промежутке")
    @Test
    void getEvents() throws Exception {
        doReturn(List.of(event())).when(service).findByDatesAndType(any(), any(), any());

        mvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "startDate": "2025-01-01",
                            "endDate": "2025-01-01",
                            "type": "CORPORATE"
                        }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "name":"testevent",
                            "description":"This is a test",
                            "date":"2025-01-01",
                            "type":"Корпоративное мероприятие"
                            }
                          ]
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен создать событие")
    @Test
    void createEvent() throws Exception {
        var date = LocalDate.of(2025, 1, 1);

        doReturn(event()).when(service).insert("testevent", "This is a test", date, EventType.CORPORATE, 1L);

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
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "id":1,
                        "name":"testevent",
                        "description":"This is a test",
                        "date":"2025-01-01",
                        "type":"Корпоративное мероприятие"
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если организатор не найден, при создании события")
    @Test
    void createEvent_OrganizerNotFound() throws Exception {
        var date = LocalDate.of(2025, 1, 1);

        doThrow(OrganizerNotFoundException.class).when(service).insert("testevent", "This is a test", date, EventType.CORPORATE, 1L);

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
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если событие не найдено, при создании события")
    @Test
    void createEvent_EventNotFound() throws Exception {
        var date = LocalDate.of(2025, 1, 1);

        doThrow(EventNotFoundException.class).when(service).insert("testevent", "This is a test", date, EventType.CORPORATE, 1L);

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
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен изменить событие")
    @Test
    void updateEvent() throws Exception {
        doReturn(event()).when(service).update(any());

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
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "id":1,
                        "name":"testevent",
                        "description":"This is a test",
                        "date":"2025-01-01",
                        "type":"Корпоративное мероприятие"
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если организатор не найден, при изменении события")
    @Test
    void updateEvent_OrganizerNotFound() throws Exception {
        doThrow(OrganizerNotFoundException.class).when(service).update(any());

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
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть ошибку с кодом 404, если событие не найдено, при изменении события")
    @Test
    void updateEvent_EventNotFound() throws Exception {
        doThrow(EventNotFoundException.class).when(service).update(any());

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
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть все события")
    @Test
    void getAdminEvents() throws Exception{
        doReturn(List.of(event())).when(service).findAll();

        mvc.perform(get("/api/admins/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "name":"testevent",
                            "description":"This is a test",
                            "date":"2025-01-01",
                            "type":"Корпоративное мероприятие"
                            }
                          ]
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен удалить событие")
    @Test
    void deleteEvent() throws Exception {
        mvc.perform(delete("/api/events/1")).andExpect(status().isOk());
    }
}