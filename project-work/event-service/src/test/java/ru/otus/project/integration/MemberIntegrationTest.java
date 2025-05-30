package ru.otus.project.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Интеграционный тест работы с пользователем")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
public class MemberIntegrationTest {
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mvc;

    @DisplayName("Должен добавить событие для пользователя")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')",
            "insert into members values (1, 'testLogin')",
            "insert into entries values (1, 1)"})
    @Test
    void addEntry() throws Exception {
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
                          "login":"testLogin",
                          "events": [
                            {
                              "id":1,
                              "name":"testName",
                              "description":"description",
                              "date":"2025-10-10",
                              "type":"Презентация"
                            }
                          ]
                        }""", JsonCompareMode.STRICT));
    }
}