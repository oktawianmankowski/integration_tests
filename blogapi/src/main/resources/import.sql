--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Andrew')
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'kowalski@domain.com', 'Jan', 'Kowalski')
insert into user (id, account_status, email, first_name, last_name) values (null, 'REMOVED', 'delete@domain.com', 'Stefan', 'Kowalski')