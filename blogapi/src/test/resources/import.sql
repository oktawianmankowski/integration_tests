--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name, last_name) values (null, 'NEW', 'test@tescik.com', 'test', 'Steward') 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'test2@test.com', 'John', 'test') 