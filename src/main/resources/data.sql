INSERT INTO application_user(login, password, balance, application_user_role, created_at)
VALUES ('admin', '$2y$10$S7MQz2uiH6uagQ.nhMqhGOkggcfyNiuB7ro3X59dQyCkD8TIGjGVW', 0.0, 'ADMIN', now());

INSERT INTO application_user(login, password, balance, application_user_role, created_at)
VALUES ('user1', '$2y$10$N4qgaS5iZUNgo.KEZ.A.heGpCQJF1rAl7fVao7SuMGuLJvPfH8btO', 0.0, 'USER', now());

INSERT INTO application_user(login, password, balance, application_user_role, created_at)
VALUES ('user2', '$2y$10$ZRf20ac83v6SXNze8/UORuVjT9beAFDqxCos/MCzFdGxNzo0znSei', 0.0, 'USER', now());

INSERT INTO order_instrument (instrument_name)
VALUES ('Agricultural Products');

INSERT INTO order_instrument (instrument_name)
VALUES ('Petroleum Products');

INSERT INTO order_instrument (instrument_name)
VALUES ('Currency');

INSERT INTO order_instrument (instrument_name)
VALUES ('Securities');

INSERT INTO order_instrument (instrument_name)
VALUES ('Oil');