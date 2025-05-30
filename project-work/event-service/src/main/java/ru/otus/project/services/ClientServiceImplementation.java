package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Client;
import ru.otus.project.entities.Event;
import ru.otus.project.exceptions.ClientNotFoundException;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.repositories.ClientRepository;
import ru.otus.project.repositories.EventRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientServiceImplementation implements ClientService {
    private final ClientRepository repository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public Client insert(String login) {
        var client = new Client();
        client.setLogin(login);

        return repository.save(client);
    }

    @Transactional
    @Override
    public Client signUp(Long id, Long idEvent) {
        var client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент под идентификатором " + id + " не найден"));
        var event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Событие под идентификатором " + idEvent + " не найден"));

        var events = client.getEvents();
        var contains = false;
        for (var contain : events) {
            if (contain.getId().equals(idEvent)) {
                contains = true;
            }
        }
        if (!contains) {
            events.add(event);
        }
        client.setEvents(events);

        return repository.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> lookOnEvents(Long id) {
        var client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент под идентификатором " + id + " не найден"));

        return client.getEvents();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findMembersOfEvent(Long idEvent) {
        return repository.findAllByEventId(idEvent);
    }

    @Transactional
    @Override
    public void deleteClient(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public Client updateClient(Long id, String login, List<Long> idEvents) {
        var client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент под идентификатором " + id + " не найден"));

        client.setLogin(login);
        client.setEvents(eventRepository.findAllById(idEvents));

        return repository.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент под идентификатором " + id + " не найден"));
    }
}
