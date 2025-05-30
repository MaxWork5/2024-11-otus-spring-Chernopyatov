package ru.otus.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Настройка авторизации только для апи с нужным путем.
        http.securityMatchers(matchers -> matchers.requestMatchers("/api/**"))
                .authorizeHttpRequests(registry -> registry.anyRequest().authenticated());
        http.oauth2ResourceServer(customizer -> customizer.jwt(Customizer.withDefaults()));

        return http.build();
    }
}