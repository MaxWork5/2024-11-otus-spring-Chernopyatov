package ru.otus.hw.services;

import org.springframework.security.acls.model.Permission;
import ru.otus.hw.domain.Book;

import java.util.List;

public interface AclServiceWrapperService {
    void createPermission(Object object, List<Permission> permissions);

    List<Book> filterBooks(List<Book> books);
}