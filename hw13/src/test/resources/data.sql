insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3), ('for_deleting', 1);

insert into comments(description, book_id)
values ('Comment 1', 1),
       ('Comment 2', 2),
       ('Comment 3', 1),
       ('for deleting', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6),
       (4,1),    (4,4);

INSERT INTO acl_sid (id, principal, sid) VALUES
                                             (1, 1, 'admin'),
                                             (2, 1, 'user'),
                                             (3, 0, 'ROLE_ADMIN');

INSERT INTO acl_class (id, class) VALUES
                                      (1, 'ru.otus.hw.domain.Author'),
                                      (2, 'ru.otus.hw.domain.Genre'),
                                      (3, 'ru.otus.hw.domain.Book'),
                                      (4, 'ru.otus.hw.domain.Comment');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
                                                                                                                        (1, 1, NULL, 3, 0),
                                                                                                                        (1, 2, NULL, 3, 0),
                                                                                                                        (1, 3, NULL, 3, 0),
                                                                                                                        (2, 1, NULL, 3, 0),
                                                                                                                        (2, 2, NULL, 3, 0),
                                                                                                                        (2, 3, NULL, 3, 0),
                                                                                                                        (2, 4, NULL, 3, 0),
                                                                                                                        (2, 5, NULL, 3, 0),
                                                                                                                        (2, 6, NULL, 3, 0),
                                                                                                                        (3, 1, NULL, 3, 0),
                                                                                                                        (3, 2, NULL, 3, 0),
                                                                                                                        (3, 3, NULL, 3, 0),
                                                                                                                        (4, 1, NULL, 3, 0),
                                                                                                                        (4, 2, NULL, 3, 0),
                                                                                                                        (4, 3, NULL, 3, 0);


INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
                                                                   (1, 1, 1, 1, 1, 1, 1),     --acl: user for Author_1
                                                                   (1, 2, 2, 1, 1, 1, 1),     --acl: admin for Author_1
                                                                   (1, 3, 3, 1, 1, 1, 1),     --acl: ROLE_ADMIN for Author_1
                                                                   (2, 1, 1, 1, 1, 1, 1),     --acl: user for Author_2
                                                                   (2, 2, 2, 1, 1, 1, 1),     --acl: admin for Author_2
                                                                   (2, 3, 3, 1, 1, 1, 1),     --acl: ROLE_ADMIN for Author_2
                                                                   (3, 1, 1, 1, 1, 1, 1),     --acl: user for Author_3
                                                                   (3, 2, 2, 1, 1, 1, 1),     --acl: admin for Author_3
                                                                   (3, 3, 3, 1, 1, 1, 1),     --acl: ROLE_ADMIN for Author_3
                                                                   (4, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_1
                                                                   (4, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_1
                                                                   (4, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_1
                                                                   (5, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_2
                                                                   (5, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_2
                                                                   (5, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_2
                                                                   (6, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_3
                                                                   (6, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_3
                                                                   (6, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_3
                                                                   (7, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_4
                                                                   (7, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_4
                                                                   (7, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_4
                                                                   (8, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_5
                                                                   (8, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_5
                                                                   (8, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_5
                                                                   (9, 1, 1, 1, 1, 1, 1),    --acl: user for Genre_6
                                                                   (9, 2, 2, 1, 1, 1, 1),    --acl: admin for Genre_6
                                                                   (9, 3, 3, 1, 1, 1, 1),    --acl: ROLE_ADMIN for Genre_6
                                                                   (10, 1, 1, 1, 1, 1, 1),   --acl: user for BookTitle_1
                                                                   (10, 2, 1, 2, 1, 1, 1),   --acl: user for BookTitle_1
                                                                   (10, 3, 1, 8, 1, 1, 1),   --acl: user for BookTitle_1
                                                                   (10, 4, 2, 1, 1, 1, 1),   --acl: admin for BookTitle_1
                                                                   (10, 5, 2, 2, 1, 1, 1),   --acl: admin for BookTitle_1
                                                                   (10, 6, 2, 8, 1, 1, 1),   --acl: admin for BookTitle_1
                                                                   (10, 7, 3, 1, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_1
                                                                   (10, 8, 3, 2, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_1
                                                                   (10, 9, 3, 8, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_1
                                                                   (11, 1, 1, 1, 1, 1, 1),   --acl: user for BookTitle_2
                                                                   (11, 2, 1, 2, 1, 1, 1),   --acl: user for BookTitle_2
                                                                   (11, 3, 1, 8, 1, 1, 1),   --acl: user for BookTitle_2
                                                                   (11, 4, 2, 1, 1, 1, 1),   --acl: admin for BookTitle_2
                                                                   (11, 5, 2, 2, 1, 1, 1),   --acl: admin for BookTitle_2
                                                                   (11, 6, 2, 8, 1, 1, 1),   --acl: admin for BookTitle_2
                                                                   (11, 7, 3, 1, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_2
                                                                   (11, 8, 3, 2, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_2
                                                                   (11, 9, 3, 8, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_2
                                                                   (12, 1, 1, 1, 1, 1, 1),   --acl: user for BookTitle_3
                                                                   (12, 2, 1, 2, 1, 1, 1),   --acl: user for BookTitle_3
                                                                   (12, 3, 1, 8, 1, 1, 1),   --acl: user for BookTitle_3
                                                                   (12, 4, 2, 1, 1, 1, 1),   --acl: admin for BookTitle_3
                                                                   (12, 5, 2, 2, 1, 1, 1),   --acl: admin for BookTitle_3
                                                                   (12, 6, 2, 8, 1, 1, 1),   --acl: admin for BookTitle_3
                                                                   (12, 7, 3, 1, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_3
                                                                   (12, 8, 3, 2, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_3
                                                                   (12, 9, 3, 8, 1, 1, 1),   --acl: ROLE_ADMIN for BookTitle_3
                                                                   (13, 1, 1, 1, 1, 1, 1),   --acl: user for Comment_1
                                                                   (13, 2, 2, 1, 1, 1, 1),   --acl: admin for Comment_1
                                                                   (13, 3, 3, 1, 1, 1, 1),   --acl: ROLE_ADMIN for Comment_1
                                                                   (14, 1, 1, 1, 1, 1, 1),   --acl: user for Comment_2
                                                                   (14, 2, 2, 1, 1, 1, 1),   --acl: admin for Comment_2
                                                                   (14, 3, 3, 1, 1, 1, 1),   --acl: ROLE_ADMIN for Comment_2
                                                                   (15, 1, 1, 1, 1, 1, 1),   --acl: user for Comment_3
                                                                   (15, 2, 2, 1, 1, 1, 1),   --acl: admin for Comment_3
                                                                   (15, 3, 3, 1, 1, 1, 1);   --acl: ROLE_ADMIN for Comment_3