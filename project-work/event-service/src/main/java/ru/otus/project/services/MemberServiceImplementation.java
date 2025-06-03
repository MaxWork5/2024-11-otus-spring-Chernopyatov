package ru.otus.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.entities.Member;
import ru.otus.project.entities.Event;
import ru.otus.project.exceptions.MemberNotFoundException;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.repositories.MemberRepository;
import ru.otus.project.repositories.EventRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceImplementation implements MemberService {
    private final MemberRepository repository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public Member insert(String login) {
        var client = new Member();
        client.setLogin(login);

        return repository.save(client);
    }

    @Transactional
    @Override
    public Member signUp(Long id, Long idEvent) {
        var client = repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Клиент под идентификатором " + id + " не найден"));
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
                .orElseThrow(() -> new MemberNotFoundException("Клиент под идентификатором " + id + " не найден"));

        return client.getEvents();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Member> findMembersOfEvent(Long idEvent) {
        return eventRepository.findById(idEvent)
                .map(Event::getMembers)
                .orElseThrow(() -> new EventNotFoundException("Событие по идентификатору " + idEvent + " не найдено"));
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Transactional
    @Override
    public Member updateMember(Long id, String login, List<Long> idEvents) {
        var client = repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Клиент под идентификатором " + id + " не найден"));

        client.setLogin(login);
        client.setEvents(eventRepository.findAllById(idEvents));

        return repository.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Клиент под идентификатором " + id + " не найден"));
    }
}
