package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.entities.Member;

/**
 * Репозиторий для работы с клиентами.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * Сохранение данных клиента.
     *
     * @param member данные клиента.
     * @return сохраненные данные клиента.
     */
    @SuppressWarnings("all")
    Member save(Member member);
}
