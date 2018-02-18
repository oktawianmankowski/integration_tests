--this script initiates db for integration tests
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name, last_name) values (null, 'NEW', 'bart@onet.com', 'test', 'Bart')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'adam@interia.com', 'Adam', 'Maria')
insert into user (id, account_status, email, first_name, last_name) values (null, 'REMOVED', 'marcin@gmail.com', 'Marcin', 'John')