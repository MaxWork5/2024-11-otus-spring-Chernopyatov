package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Review;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.ReviewNotFoundException;
import ru.otus.project.repositories.EventRepository;
import ru.otus.project.repositories.ReviewRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImplementation implements ReviewService {
    private final ReviewRepository repository;

    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public Review insert(String comment, Long eventId) {
        return eventRepository.findById(eventId)
                .map(event -> {
                    var review = new Review(comment, eventId);
                    return repository.save(review);
                })
                .orElseThrow(() -> new EventNotFoundException("Событие под идентификатором " + eventId + " не найдено"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> findReviews(List<Long> eventId) {
        return repository.findAllByEventIdInOrderByEventId(eventId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Review findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ReviewNotFoundException("Отзыва под идентификатором " + id + " не найдено"));
    }

    @Transactional
    @Override
    public Review update(Long id, String comment) {
        var review = repository.findById(id).orElseThrow(() ->
                new ReviewNotFoundException("Отзыва под идентификатором " + id + " не найдено"));
        review.setComment(comment);

        return repository.save(review);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
