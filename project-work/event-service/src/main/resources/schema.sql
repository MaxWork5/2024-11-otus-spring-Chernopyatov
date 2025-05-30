create table clients (
                         id      bigserial,
                         login   varchar(255) unique,
                         primary key (id)
);

create table organizer (
                           id      bigserial,
                           login   varchar(255) unique,
                           primary key (id)
);

create table events (
    id              bigserial,
    name            varchar(255),
    description     text,
    date_of_event   date,
    type_of_event   varchar(255),
    organizer_id    bigint references organizer(id),
    primary key (id)
);

create table entries (
    event_id   bigint references events(id),
    client_id  bigint references clients(id),
    primary key (events_id, clients_id)
);

create table reviews (
    id         bigserial,
    comment    text,
    event_id   bigint references events(id),
    primary key (id)
)