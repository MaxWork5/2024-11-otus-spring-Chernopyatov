package ru.otus.project.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.otus.project.dto.requests.ClientSendRequestDTO;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.exceptions.NotFoundException;

@RequiredArgsConstructor
public class RestEventServiceClient implements EventServiceClient {

    private final RestClient client;

    @Override
    public ClientSendResponseDTO sendPostRequest(String path, ClientSendRequestDTO body) {
        try {
            return client.post()
                    .uri(path)
                    .body(body)
                    .retrieve()
                    .body(ClientSendResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public ClientSendResponseDTO sendPutRequest(String path, Long id, ClientSendRequestDTO body) {
        try {
            return client.put()
                    .uri(path, id)
                    .body(body)
                    .retrieve()
                    .body(ClientSendResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public ClientSendResponseDTO sendGetRequest(String path, String id) {
        try {
            return client.get()
                    .uri(path, id)
                    .retrieve()
                    .body(ClientSendResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void sendDeleteRequest(String path, String id) {
        client.delete()
                .uri(path, id)
                .retrieve()
                .toEntity(Void.class);
    }
}
