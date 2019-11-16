create table addresses (address_id integer not null auto_increment, city varchar(255) not null, country varchar(255) not null, number varchar(255) not null, postcode varchar(10) not null, street varchar(255), primary key (address_id)) engine=InnoDB
create table event_game (game_id integer not null, event_id integer not null, primary key (game_id, event_id)) engine=InnoDB
create table event_product (product_id integer not null, event_id integer not null, primary key (product_id, event_id)) engine=InnoDB
create table event_user (user_id bigint not null, event_id integer not null, primary key (user_id, event_id)) engine=InnoDB
create table events (event_id integer not null auto_increment, name varchar(255) not null, address_id integer, primary key (event_id)) engine=InnoDB
create table games (game_id integer not null auto_increment, description varchar(255), name varchar(255) not null, primary key (game_id)) engine=InnoDB
create table product_categories (product_category_id integer not null auto_increment, name varchar(255) not null, primary key (product_category_id)) engine=InnoDB
create table products (product_id integer not null auto_increment, name varchar(255) not null, prize double precision, product_category_id integer, primary key (product_id)) engine=InnoDB
create table roles (role_id integer not null auto_increment, name varchar(255) not null, primary key (role_id)) engine=InnoDB
create table user_cards (user_card_id integer not null auto_increment, event_id bigint, product_id bigint, user_id bigint, primary key (user_card_id)) engine=InnoDB
create table users (user_id bigint not null auto_increment, email varchar(255), enabled boolean default false, name varchar(255), password varchar(60) not null, surname varchar(255), username varchar(255) not null, role_id integer, primary key (user_id)) engine=InnoDB
alter table roles add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name)
alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email)
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username)
alter table event_game add constraint FKj9qv8nicwevldahy2piaqsggb foreign key (event_id) references events (event_id)
alter table event_game add constraint FKgilpbrdryk43y5s42eif4348 foreign key (game_id) references games (game_id)
alter table event_product add constraint FKqxg4e8f1wecxbt1unog69jrbd foreign key (event_id) references events (event_id)
alter table event_product add constraint FKjm43yywayh25xybx3pqal8t0g foreign key (product_id) references products (product_id)
alter table event_user add constraint FKdip9gnrj5k5vd2jlyjmxrnlch foreign key (event_id) references events (event_id)
alter table event_user add constraint FK45uvxaov8fbham8l63a7jkbyr foreign key (user_id) references users (user_id)
alter table events add constraint FKquc7xx27bo60lupj2rf7e0hn2 foreign key (address_id) references addresses (address_id)
alter table products add constraint FKe05mpyhp4howtq8q4s65wm3q8 foreign key (product_category_id) references product_categories (product_category_id)
alter table user_cards add constraint FKgau6xbkn1pkhs7dqgkx4a2xba foreign key (event_id) references users (user_id)
alter table user_cards add constraint FK7tlo3mimbj9w8f4r8xy1qrmum foreign key (product_id) references users (user_id)
alter table user_cards add constraint FK55ime3genywh3rg5yu62hmfvy foreign key (user_id) references users (user_id)
alter table users add constraint FKp56c1712k691lhsyewcssf40f foreign key (role_id) references roles (role_id)
create table addresses (address_id integer not null auto_increment, city varchar(255) not null, country varchar(255) not null, number varchar(255) not null, postcode varchar(10) not null, street varchar(255), primary key (address_id)) engine=InnoDB
create table event_game (game_id integer not null, event_id integer not null, primary key (game_id, event_id)) engine=InnoDB
create table event_product (product_id integer not null, event_id integer not null, primary key (product_id, event_id)) engine=InnoDB
create table event_user (user_id bigint not null, event_id integer not null, primary key (user_id, event_id)) engine=InnoDB
create table events (event_id integer not null auto_increment, name varchar(255) not null, address_id integer, primary key (event_id)) engine=InnoDB
create table games (game_id integer not null auto_increment, description varchar(255), name varchar(255) not null, primary key (game_id)) engine=InnoDB
create table product_categories (product_category_id integer not null auto_increment, name varchar(255) not null, primary key (product_category_id)) engine=InnoDB
create table products (product_id integer not null auto_increment, name varchar(255) not null, prize double precision, product_category_id integer, primary key (product_id)) engine=InnoDB
create table roles (role_id integer not null auto_increment, name varchar(255) not null, primary key (role_id)) engine=InnoDB
create table user_cards (user_card_id integer not null auto_increment, event_id bigint, product_id bigint, user_id bigint, primary key (user_card_id)) engine=InnoDB
create table users (user_id bigint not null auto_increment, email varchar(255), enabled boolean default false, name varchar(255), password varchar(60) not null, surname varchar(255), username varchar(255) not null, role_id integer, primary key (user_id)) engine=InnoDB
alter table roles add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name)
alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email)
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username)
alter table event_game add constraint FKj9qv8nicwevldahy2piaqsggb foreign key (event_id) references events (event_id)
alter table event_game add constraint FKgilpbrdryk43y5s42eif4348 foreign key (game_id) references games (game_id)
alter table event_product add constraint FKqxg4e8f1wecxbt1unog69jrbd foreign key (event_id) references events (event_id)
alter table event_product add constraint FKjm43yywayh25xybx3pqal8t0g foreign key (product_id) references products (product_id)
alter table event_user add constraint FKdip9gnrj5k5vd2jlyjmxrnlch foreign key (event_id) references events (event_id)
alter table event_user add constraint FK45uvxaov8fbham8l63a7jkbyr foreign key (user_id) references users (user_id)
alter table events add constraint FKquc7xx27bo60lupj2rf7e0hn2 foreign key (address_id) references addresses (address_id)
alter table products add constraint FKe05mpyhp4howtq8q4s65wm3q8 foreign key (product_category_id) references product_categories (product_category_id)
alter table user_cards add constraint FKgau6xbkn1pkhs7dqgkx4a2xba foreign key (event_id) references users (user_id)
alter table user_cards add constraint FK7tlo3mimbj9w8f4r8xy1qrmum foreign key (product_id) references users (user_id)
alter table user_cards add constraint FK55ime3genywh3rg5yu62hmfvy foreign key (user_id) references users (user_id)
alter table users add constraint FKp56c1712k691lhsyewcssf40f foreign key (role_id) references roles (role_id)
