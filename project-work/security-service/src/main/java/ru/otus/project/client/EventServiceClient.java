package ru.otus.project.client;

import ru.otus.project.dto.requests.ClientSendRequestDTO;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.exceptions.NotFoundException;

/**
 * Клиент для работы с сервисом по организации событий.
 */
public interface EventServiceClient {
    /**
     * Отправка POST запроса в сервис по работе с событиями.
     *
     * @param path путь запроса.
     * @param body тело запроса.
     * @return ответное сообщение от сервиса.
     * @throws NotFoundException в случае получения ошибки с кодом 404 со стороны сервиса.
     */
    ClientSendResponseDTO sendPostRequest(String path, ClientSendRequestDTO body) throws NotFoundException;

    /**
     * Отправка PUT запроса в сервис по работе с событиями.
     *
     * @param path путь запроса.
     * @param id   идентификатор изменяемой сущности.
     * @param body тело запроса.
     * @return ответное сообщение от сервиса.
     * @throws NotFoundException в случае получения ошибки с кодом 404 со стороны сервиса.
     */
    ClientSendResponseDTO sendPutRequest(String path, Long id, ClientSendRequestDTO body) throws NotFoundException;

    /**
     * Отправка GET запроса в сервис по работе с событиями.
     *
     * @param path путь запроса.
     * @param id   идентификатор сущности.
     * @return ответное сообщение от сервиса.
     * @throws NotFoundException в случае получения ошибки с кодом 404 со стороны сервиса.
     */
    ClientSendResponseDTO sendGetRequest(String path, String id) throws NotFoundException;

    /**
     * Отправка DELETE запроса в сервис по работе с событиями.
     *
     * @param path путь запроса.
     * @param id   идентификатор удаляемой сущности.
     * @throws NotFoundException в случае получения ошибки с кодом 404 со стороны сервиса.
     */
    void sendDeleteRequest(String path, String id);
}
