create table category (
	id integer generated by default as identity, 
	category_name varchar(255), 
	description varchar(255), 
	image_url varchar(255), 
	primary key (id)
);
create table products (
	id integer generated by default as identity,
	description varchar(255), 
	imageurl varchar(255), 
	name varchar(255), 
	price double precision not null, 
	category_id integer, 
	primary key (id)
);
