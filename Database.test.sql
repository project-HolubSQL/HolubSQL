create database Dbase

create table address \
	(addrId int, street varchar, city varchar, \
	state char(2), zip int, primary key(addrId))

create table name(first varchar(10), last varchar(10), addrId integer)

insert into address values( 0,'12 MyStreet','Berkeley','CA','99999')
insert into address values( 1, '34 Quarry Ln.', 'Bedrock' , 'XX', '00000')

insert into name VALUES ('Fred',  'Flintstone', '2')
insert into name VALUES ('Wilma', 'Flintstone', '1')
insert into name (last,first,addrId) VALUES('Holub','Allen',(10-10*1))

select * from address, name where address.addrId = name.addrId

select street from address, name where address.addrId = name.addrId


create table users (username varchar(10), password varchar(10), \
	primary key(username))


insert into users values('Fred','Flintstone')
insert into users values('Wilma','Flintstone')


create table buys (id int, price int, quantity int, \
	primary key(id))

insert into buys values(0, 500, 5)
select * from buys 

create table sells (id int, price int, quantity int, \
	primary key(id))

insert into sells values(0, 1000, 5)



select * from name order by addrId desc