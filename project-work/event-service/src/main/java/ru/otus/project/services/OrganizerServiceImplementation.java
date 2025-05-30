package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.Organizer;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.repositories.EventRepository;
import ru.otus.project.repositories.OrganizerRepository;
import ru.otus.project.repositories.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrganizerServiceImplementation implements OrganizerService {
    private final OrganizerRepository repository;

    private final EventRepository eventRepository;

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Organizer> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Organizer findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new OrganizerNotFoundException("Организатор по идентификатору " + id + " не найден"));
    }

    @Transactional
    @Override
    public Organizer insert(String login) {
        var organizer = new Organizer();
        organizer.setLogin(login);

        return repository.save(organizer);
    }

    @Transactional
    @Override
    public Organizer update(Long id, String login) {
        if (repository.existsById(id)) {
            var organizer = new Organizer();
            organizer.setId(id);
            organizer.setLogin(login);

            return repository.save(organizer);
        }
        throw new OrganizerNotFoundException("Организатор по идентификатору " + id + " не найден");
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(organizer -> {
            var events = eventRepository.findAllByOrganizerId(organizer.getId());
            reviewRepository.deleteAllByEventIdIn(events.stream().map(Event::getId).collect(Collectors.toList()));
            eventRepository.deleteAll(events);
            repository.delete(organizer);
        });
    }
}
