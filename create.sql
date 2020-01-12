create table address (address_id integer not null auto_increment, city varchar(255) not null, country varchar(255) not null, number varchar(255) not null, postcode varchar(10) not null, street varchar(255), primary key (address_id)) engine=InnoDB
create table event (event_id integer not null auto_increment, name varchar(255) not null, start_date date not null, address_id integer, primary key (event_id)) engine=InnoDB
create table event_game (game_id integer not null, event_id integer not null, primary key (game_id, event_id)) engine=InnoDB
create table event_product (product_id integer not null, event_id integer not null, primary key (product_id, event_id)) engine=InnoDB
create table event_user (event_user_id integer not null auto_increment, is_admin boolean default false not null, event_id integer, user_id bigint, primary key (event_user_id)) engine=InnoDB
create table game (game_id integer not null auto_increment, description varchar(255), name varchar(255) not null, primary key (game_id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
create table product (product_id integer not null auto_increment, name varchar(255) not null, price double precision, product_category_id integer, primary key (product_id)) engine=InnoDB
create table product_category (product_category_id integer not null auto_increment, name varchar(255) not null, primary key (product_category_id)) engine=InnoDB
create table role (role_id integer not null auto_increment, name varchar(255) not null, primary key (role_id)) engine=InnoDB
create table user (user_id bigint not null auto_increment, email varchar(255) not null, enabled boolean default false, name varchar(255), password varchar(60) not null, surname varchar(255), username varchar(255) not null, role_id integer, primary key (user_id)) engine=InnoDB
create table user_card (user_card_id integer not null auto_increment, event_user_id integer, product_id integer, primary key (user_card_id)) engine=InnoDB
create table verification_token (id bigint not null, expiry_date datetime(6), token varchar(255), user_id bigint not null, primary key (id)) engine=InnoDB
alter table role drop index UK_8sewwnpamngi6b1dwaa88askk
alter table role add constraint UK_8sewwnpamngi6b1dwaa88askk unique (name)
alter table user drop index UK_ob8kqyqqgmefl0aco34akdtpe
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email)
alter table user drop index UK_sb8bbouer5wak8vyiiy4pf2bx
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)
alter table event add constraint FKbnsudi2pgjak7feycwo8297xi foreign key (address_id) references address (address_id)
alter table event_game add constraint FKp4t1tprvr55pd8jaauwscb732 foreign key (event_id) references event (event_id)
alter table event_game add constraint FK71oudjl7kxm72xjcp5srwb13y foreign key (game_id) references game (game_id)
alter table event_product add constraint FK4ad8tyf4bpqr6h821jh42uqxl foreign key (event_id) references event (event_id)
alter table event_product add constraint FKntuxn8awf9tn7rxvximxbhb2x foreign key (product_id) references product (product_id)
alter table event_user add constraint FKtc58o1e7bpugjcxuqr8l05l12 foreign key (event_id) references event (event_id)
alter table event_user add constraint FK67g39uhr99s8ney3d8tccqtf6 foreign key (user_id) references user (user_id)
alter table product add constraint FKcwclrqu392y86y0pmyrsi649r foreign key (product_category_id) references product_category (product_category_id)
alter table user add constraint FKn82ha3ccdebhokx3a8fgdqeyy foreign key (role_id) references role (role_id)
alter table user_card add constraint FKcftxjqhhcni4vnmgw58k83sjj foreign key (event_user_id) references event_user (event_user_id)
alter table user_card add constraint FKagbamur6ryad61kxljjsuwrw5 foreign key (product_id) references product (product_id)
alter table verification_token add constraint FKrdn0mss276m9jdobfhhn2qogw foreign key (user_id) references user (user_id)
insert into role values (1,'USER'),(2,'ADMIN');