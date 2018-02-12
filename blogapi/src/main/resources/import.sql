--this script initiates db for h2 db (used in test profile)
insert into user (id, account_status, email, first_name, last_name) values (null, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (null, 'NEW', 'brian@domain.com', 'Brian')
insert into user (id, account_status, email, first_name) values (1000, 'CONFIRMED', 'justyna@domain.com', 'Justyna')
insert into user (id, account_status, email, first_name) values (2000, 'NEW', 'jan@domain.com', 'Jan')
insert into user (id, account_status, email, first_name) values (3000, 'CONFIRMED', 'maria@domain.com', 'Maria')
insert into user (id, account_status, email, first_name) values (4000, 'REMOVED', 'marianna@domain.com', 'Maria')

insert into blog_post (id, user_id, entry) values (1000, 1000, 'post 1')