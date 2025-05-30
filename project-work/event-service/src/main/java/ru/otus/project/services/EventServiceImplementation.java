package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.UpdateEvent;
import ru.otus.project.entities.enums.EventType;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.repositories.EventRepository;
import ru.otus.project.repositories.OrganizerRepository;
import ru.otus.project.repositories.ReviewRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImplementation implements EventService {
    private final ReviewRepository reviewRepository;

    private final EventRepository repository;

    private final OrganizerRepository organizerRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Event> findEventsForSignUp() {
        return repository.findAllByDateAfter(LocalDate.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findByDatesAndType(LocalDate startDate, LocalDate endDate, EventType type) {
        return repository.findAllByDateBetweenAndType(startDate, endDate, type);
    }

    @Transactional
    @Override
    public Event insert(String name, String description, LocalDate date, EventType type, Long organizerId) {
        return save(new UpdateEvent(null, name, description, date, type, organizerId));
    }

    @Transactional
    @Override
    public Event update(UpdateEvent updateEvent) {
        return save(updateEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(event -> {
            reviewRepository.deleteAllByEventId(id);
            repository.delete(event);
        });
    }

    private Event save(UpdateEvent updateEvent) {
        if (! organizerRepository.existsById(updateEvent.organizerId())) {
            throw new OrganizerNotFoundException("Организатор по идентификатору " + updateEvent.id() + " не найден");
        }

        var event = new Event(updateEvent.name(),
                updateEvent.description(),
                updateEvent.date(),
                updateEvent.type(),
                updateEvent.organizerId());

        if (updateEvent.id() != null) {
            if (repository.existsById(updateEvent.id())) {
                event.setId(updateEvent.id());
            } else {
                throw new EventNotFoundException("Событие по идентификатору " + updateEvent.id() + " не найдено");
            }
        }

        return repository.save(event);
    }
}
