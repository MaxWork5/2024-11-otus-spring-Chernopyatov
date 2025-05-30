package ru.otus.project.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства для подключения к сервису по работе с событиями.
 */
@Data
@ConfigurationProperties("event-service")
public class EventServiceProperties {
    /**
     * Адрес сервиса по работе с событиями.
     */
    private String baseUrl;
}
