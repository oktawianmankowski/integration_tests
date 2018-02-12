--this script initiates db for integration tests 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name, last_name) values (null, 'NEW', 'test3@testTestTescik.com', 'test', 'Steward') 
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'test4@testTescik.com', 'John', 'test')
insert into user (id, account_status, email, first_name, last_name) values (null, 'REMOVED', 'test5@testTescik.com', 'John', 'test') 