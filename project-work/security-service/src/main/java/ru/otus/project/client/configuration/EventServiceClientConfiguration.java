package ru.otus.project.client.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.otus.project.client.RestEventServiceClient;

@Configuration
@EnableConfigurationProperties(EventServiceProperties.class)
public class EventServiceClientConfiguration {

    @Bean
    RestEventServiceClient eventServiceClient(RestClient.Builder builder, EventServiceProperties properties) {
        return new RestEventServiceClient(builder.baseUrl(properties.getBaseUrl()).build());
    }
}
