package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.entities.jpa.JpaComment;

public interface JpaCommentRepository  extends JpaRepository<JpaComment, Long> {
}