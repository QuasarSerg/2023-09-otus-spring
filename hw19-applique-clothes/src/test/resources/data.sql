insert into products(full_name, type, product_size, price)
values ('Product_1', 'Футболка', 'XL', 10),
        ('Product_2', 'Футболка', 'XL', 10),
        ('Product_3', 'Футболка', 'XL', 10);

insert into orders(product_id, amount, price, applique)
values (1, 5, 10, 'Applique_1'),
        (2, 5, 10, 'Applique_2'),
        (3, 5, 10, 'Applique_3');

insert into users(username, password, authority)
values ('user', '$2a$12$jjGkt67AbJk70QUdFOhdweeuj2RFQXCMscNo2X4BGz/OzYb7HuHAe', 'ROLE_USER'),
       ('admin', '$2a$12$jjGkt67AbJk70QUdFOhdweeuj2RFQXCMscNo2X4BGz/OzYb7HuHAe', 'ROLE_ADMIN');