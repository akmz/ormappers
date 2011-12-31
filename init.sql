drop all objects;

create table user (
	id identity
,	name varchar(20)
,	version bigint not null default(1)
);

insert into user (name) values
	('Alice')
,	('Bob')
,	('Carol')
,	('Dave')
,	('Ellen')
,	('Frank')
;

create table book (
	id identity
,	title varchar(100)
,	publisher_id bigint
);

create table author (
	id identity
,	name varchar(100)
);

create table author_book (
	author_id bigint
,	book_id bigint
);

create table publisher (
	id identity
,	name varchar(100)
,	address_id bigint
);

create table address (
	id identity
,	address varchar(100)
);

insert into address (address) values
 ('address1')
;

insert into publisher (name, address_id) values
 ('publisher1', 1)
;

insert into book (title, publisher_id) values
 ('book1', 1)
,('book2', 1)
;

insert into author (name) values
 ('author1')
,('author2')
;

insert into author_book (author_id, book_id) values
 (1, 1)
,(1, 2)
,(2, 1)
,(2, 2)
;

create table complex (
	id1 bigint
,	id2 bigint
,	val varchar(100)
,	primary key(id1, id2)
);

insert into complex (id1, id2, val) values
 (1, 1, 'a')
,(1, 2, 'b')
,(1, 3, 'c')
,(2, 1, 'd')
,(2, 2, 'e')
;

create table listener_sample (
	id identity,
	val varchar(100),
	version bigint not null default(1)
);

create table listener_sample2 (
	id identity,
	val varchar(100),
	version bigint not null default(1)
);