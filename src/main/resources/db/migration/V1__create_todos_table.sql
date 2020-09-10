create table if not exists todos
(
    id          serial primary key,
    title       varchar,
    description text
);
