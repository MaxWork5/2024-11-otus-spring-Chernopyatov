package ru.otus.project.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.otus.project.client.configuration.EventServiceClientConfiguration;
import ru.otus.project.dto.requests.member.MemberInsertRequestDTO;
import ru.otus.project.dto.requests.member.MemberUpdateRequestDTO;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.exceptions.NotFoundException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("Тест клиента сервиса событий")
@RestClientTest(components = {EventServiceClientConfiguration.class})
class RestEventServiceClientTest {

    @Autowired
    MockRestServiceServer mock;
    @Autowired
    RestEventServiceClient client;

    @DisplayName("Должен вернуть результат POST запроса в сервис по работе с событиями")
    @Test
    void sendPostRequest() {
        mock.expect(method(HttpMethod.POST))
                .andExpect(requestTo("http://localhost:8082/api/clients"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "login": "kanajmk"
                        }""", JsonCompareMode.STRICT))
                .andRespond(withSuccess("""
                        {
                            "id": 1,
                            "login": "kanajmk"
                        }""", MediaType.APPLICATION_JSON));

        assertThat(client.sendPostRequest("/api/clients", new MemberInsertRequestDTO("kanajmk")))
                .isEqualTo(new ClientSendResponseDTO(null, 1L, "kanajmk", null, null,
                        null, null,null,null,null));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404 при POST запросе в сервис по работе с событиями")
    @Test
    void sendPostRequest_notFound() {
        mock.expect(method(HttpMethod.POST))
                .andExpect(requestTo("http://localhost:8082/api/clients"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "login": "kanajmk"
                        }""", JsonCompareMode.STRICT))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.sendPostRequest("/api/clients", new MemberInsertRequestDTO("kanajmk")))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Должен вернуть результат PUT запроса в сервис по работе с событиями")
    @Test
    void sendPutRequest() {
        mock.expect(method(HttpMethod.PUT))
                .andExpect(requestTo("http://localhost:8082/api/clients/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "login": "kanajmk",
                          "idEvents": []
                        }""", JsonCompareMode.STRICT))
                .andRespond(withSuccess("""
                        {
                            "id": 1,
                            "login": "kanajmk"
                        }""", MediaType.APPLICATION_JSON));

        assertThat(client.sendPutRequest("/api/clients/{id}", 1L, new MemberUpdateRequestDTO("kanajmk", List.of())))
                .isEqualTo(new ClientSendResponseDTO(null, 1L, "kanajmk", null, null,
                        null, null,null,null,null));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404 при PUT запросе в сервис по работе с событиями")
    @Test
    void sendPutRequest_notFound() {
        mock.expect(method(HttpMethod.PUT))
                .andExpect(requestTo("http://localhost:8082/api/clients/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "login": "kanajmk",
                          "idEvents": []
                        }""", JsonCompareMode.STRICT))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.sendPutRequest("/api/clients/{id}", 1L, new MemberUpdateRequestDTO("kanajmk", List.of())))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Должен вернуть результат GET запроса в сервис по работе с событиями")
    @Test
    void sendGetRequest() {
        mock.expect(method(HttpMethod.GET))
                .andExpect(requestTo("http://localhost:8082/api/clients"))
                .andRespond(withSuccess("""
                        {
                            "id": 1,
                            "login": "kanajmk"
                        }""", MediaType.APPLICATION_JSON));

        assertThat(client.sendGetRequest("/api/clients", null))
                .isEqualTo(new ClientSendResponseDTO(null, 1L, "kanajmk", null, null,
                        null, null,null,null,null));
    }

    @DisplayName("Должен вернуть ошибку с кодом 404 при GET запросе в сервис по работе с событиями")
    @Test
    void sendGetRequest_notFound() {
        mock.expect(method(HttpMethod.GET))
                .andExpect(requestTo("http://localhost:8082/api/clients/1"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.sendGetRequest("/api/clients/{id}", "1"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Должен выполнить DELETE запрос в сервис по работе с событиями")
    @Test
    void sendDeleteRequest() {
        mock.expect(method(HttpMethod.DELETE))
                .andExpect(requestTo("http://localhost:8082/api/clients/1"))
                .andRespond(withStatus(HttpStatus.OK));

        assertThatCode(() -> client.sendDeleteRequest("/api/clients/{id}", "1")).doesNotThrowAnyException();
    }
}