package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Organizer;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.repositories.OrganizerRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizerServiceImplementation implements OrganizerService {
    private final OrganizerRepository repository;

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
        repository.deleteById(id);
    }
}
