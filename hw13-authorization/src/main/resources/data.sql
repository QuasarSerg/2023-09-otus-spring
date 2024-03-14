insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('CommentText_1', 1), ('CommentText_2', 1), ('CommentText_1', 2), ('CommentText_1', 3);

insert into comments(text, book_id)
values ('CommentText_1', 1), ('CommentText_2', 1), ('CommentText_1', 2), ('CommentText_1', 3);

insert into users(username, password, authority)
values ('user', '$2a$12$jjGkt67AbJk70QUdFOhdweeuj2RFQXCMscNo2X4BGz/OzYb7HuHAe', 'ROLE_USER'),
       ('admin', '$2a$12$jjGkt67AbJk70QUdFOhdweeuj2RFQXCMscNo2X4BGz/OzYb7HuHAe', 'ROLE_ADMIN'); --bcrypt