--liquibase formatted sql

--Expected Output:
--X

--changeSet id: 1 author: nathan
create table test (id int);

--changeSet id: 2 author: nathan
create table test2 (id int);
