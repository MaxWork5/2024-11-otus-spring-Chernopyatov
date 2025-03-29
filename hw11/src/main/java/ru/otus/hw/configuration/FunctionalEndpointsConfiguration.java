package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalEndpointsConfiguration {

    @Bean
    public RouterFunction<ServerResponse> composeRoutes(BookService service) {
        var handler = new BookHandler(service);
        return route()
                .GET("/api/books", queryParam("id", param -> param != null && !param.isEmpty()),
                        handler::findBooks)
                .GET("/api/books", accept(MediaType.APPLICATION_JSON), handler::listBooks)
                .POST("/api/books", accept(MediaType.APPLICATION_JSON), handler::insertBooks)
                .PUT("/api/books", accept(MediaType.APPLICATION_JSON), handler::editBooks)
                .DELETE("/api/books", queryParam("id", param -> param != null && !param.isEmpty()),
                        handler::deleteBooks)
                .build();
    }


    static class BookHandler {
        private final BookService service;

        public BookHandler(BookService bookService) {
            this.service = bookService;
        }

        Mono<ServerResponse> listBooks(ServerRequest request) {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(service.findAll(), BookDto.class);
        }

        Mono<ServerResponse> editBooks(ServerRequest request) {
            return request.bodyToMono(BookDto.class)
                    .flatMap(bookDto ->
                            ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(service.update(
                                            bookDto.getId(),
                                            bookDto.getTitle(),
                                            bookDto.getAuthor().getId(),
                                            bookDto.getGenreIds()), BookDto.class));
        }

        Mono<ServerResponse> insertBooks(ServerRequest request) {
            return request.bodyToMono(BookDto.class)
                    .flatMap(bookDto ->
                            ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(service.insert(
                                            bookDto.getTitle(),
                                            bookDto.getAuthor().getId(),
                                            bookDto.getGenreIds()), BookDto.class));
        }

        Mono<ServerResponse> deleteBooks(ServerRequest request) {
            return service.deleteById(request.queryParam("id")
                            .orElseThrow(IllegalArgumentException::new))
                    .then(ServerResponse.noContent().build());
        }

        Mono<ServerResponse> findBooks(ServerRequest request) {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(service.findById(request.queryParam("id")
                                    .orElseThrow(IllegalArgumentException::new)),
                            BookDto.class);
        }
    }
}
