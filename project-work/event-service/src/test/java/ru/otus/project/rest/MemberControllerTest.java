package ru.otus.project.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.project.entities.Member;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;
import ru.otus.project.exceptions.MemberNotFoundException;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.services.MemberService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера пользователей")
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @MockitoBean
    MemberService service;

    @Autowired
    MockMvc mvc;

    @DisplayName("Должен добавить событие для пользователя")
    @Test
    void addEntry() throws Exception {
        doReturn(client())
                .when(service).signUp(1L, 1L);

        mvc.perform(put("/api/entries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idEntry": 1
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "id":1,
                          "login":"testlogin",
                          "events": [
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

    @DisplayName("Должен вернуть ошибку с кодом 404 если пользователь не найден, при добавлении события пользователю")
    @Test
    void addEntry_memberNotFound() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(service).signUp(1L, 1L);

        mvc.perform(put("/api/entries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idEntry": 1
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть ошибку с кодом 404 если событие не найдено, при добавлении события пользователю")
    @Test
    void addEntry_EventNotFound() throws Exception {
        doThrow(EventNotFoundException.class)
                .when(service).signUp(1L, 1L);

        mvc.perform(put("/api/entries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idEntry": 1
                                }"""))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть список событий, в которых принимает участие пользователь")
    @Test
    void getEntry() throws Exception {
        doReturn(List.of(event()))
                .when(service).lookOnEvents(1L);

        mvc.perform(get("/api/entries/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"items":
                          [
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

    @DisplayName("Должен вернуть ошибку с кодом 404, если пользователь не найден при просмотре событий")
    @Test
    void getEntry_MemberNotFound() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(service).lookOnEvents(1L);

        mvc.perform(get("/api/entries/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Должен вернуть список всех пользователей, участвующих в событии")
    @Test
    void getMemberOfEvent() throws Exception {
        doReturn(List.of(client()))
                .when(service).findMembersOfEvent(1L);

        mvc.perform(get("/api/events/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "login":"testlogin",
                            "events":
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
                          ]
                        }""", JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть всех пользователей")
    @Test
    void getMembers() throws Exception {
        doReturn(List.of(client()))
                .when(service).findAll();

        mvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "items":
                          [
                            {
                            "id":1,
                            "login":"testlogin",
                            "events":
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
                          ]
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен вернуть пользователя по идентификатору")
    @Test
    void getMember() throws Exception {
        doReturn(client())
                .when(service).findById(1L);

        mvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "id":1,
                        "login":"testlogin",
                        "events":
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

    @DisplayName("Должен добавить пользователя")
    @Test
    void addMember() throws Exception {
        var client = client();
        client.setEvents(null);

        doReturn(client)
                .when(service).insert("testlogin");

        mvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testlogin"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "id":1,
                        "login":"testlogin",
                        "events": null
                        }
                        """, JsonCompareMode.STRICT));
    }

    @DisplayName("Должен удалить пользователя")
    @Test
    void deleteMember() throws Exception {
        mvc.perform(delete("/api/members/1"))
                .andExpect(status().isOk());
    }

    @DisplayName("Должен изменить пользователя")
    @Test
    void updateMember() throws Exception {
        doReturn(client())
                .when(service).updateMember(1L, "testlogin", List.of(1L));

        mvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testlogin",
                                    "idEvents": [1]
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                        "id":1,
                        "login":"testlogin",
                        "events":
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

    @DisplayName("Должен вернуть ошибку с кодом 404, если пользователь не найден при изменении пользователя")
    @Test
    void updateMember_memberNotFound() throws Exception {
        doThrow(MemberNotFoundException.class)
                .when(service).updateMember(1L, "testlogin", List.of(1L));

        mvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "login": "testlogin",
                                    "idEvents": [1]
                                }"""))
                .andExpect(status().isNotFound());
    }

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

    private static Member client() {
        var client = new Member();
        client.setId(1L);
        client.setLogin("testlogin");
        client.setEvents(List.of(event()));
        return client;
    }
}