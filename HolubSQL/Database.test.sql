create database Dbase

create table address \
	(addrId int, street varchar, city varchar, \
	state char(2), zip int, primary key(addrId))

create table name(first varchar(10), last varchar(10), addrId integer)

insert into address values( 0,'12 MyStreet','Berkeley','CA','99999')
insert into address values( 1, '34 Quarry Ln.', 'Bedrock' , 'XX', '00000')

insert into name VALUES ('Fred',  'Flintstone', '1')
insert into name VALUES ('Wilma', 'Flintstone', '1')
insert into name (last,first,addrId) VALUES('Holub','Allen',(10-10*1))

select * from address, name where address.addrId = name.addrId

select street from address, name where address.addrId = name.addrId