INSERT INTO application_user(id, login, password, wallet, application_user_role, created_at)
VALUES ('b44a08f1-b2ed-4470-8345-e51e3acd3ba9', 'admin',
        '{bcrypt}$2y$10$S7MQz2uiH6uagQ.nhMqhGOkggcfyNiuB7ro3X59dQyCkD8TIGjGVW', 0.0, 'ADMIN', now());

INSERT INTO application_user(id, login, password, wallet, application_user_role, created_at)
VALUES ('97b35bfb-5621-4338-b79c-15f1ed35b7d7', 'user1',
        '{bcrypt}$2y$10$N4qgaS5iZUNgo.KEZ.A.heGpCQJF1rAl7fVao7SuMGuLJvPfH8btO', 0.0, 'USER', now());

INSERT INTO application_user(id, login, password, wallet, application_user_role, created_at)
VALUES ('3541aaa9-4cd2-4982-91cb-4d1a2e319bf2', 'user2',
        '{bcrypt}$2y$10$ZRf20ac83v6SXNze8/UORuVjT9beAFDqxCos/MCzFdGxNzo0znSei', 0.0, 'USER', now());

INSERT INTO order_instrument (id, instrument_name)
VALUES ('0bd18795-0e49-471f-a1d6-ebd298a36257', 'Agricultural Products');

INSERT INTO order_instrument (id, instrument_name)
VALUES ('47c90682-dc6c-41a1-826f-b985314d66d4', 'Petroleum Products');

INSERT INTO order_instrument (id, instrument_name)
VALUES ('b9e3837e-744e-4f74-aa80-5e335e0bb3f2', 'Currency');

INSERT INTO order_instrument (id, instrument_name)
VALUES ('14e61dec-3fa1-4918-9d54-10c198a414fd', 'Securities');

INSERT INTO order_instrument (id, instrument_name)
VALUES ('4a0b0773-8dd3-4511-8379-bef3ced55081', 'Oil');