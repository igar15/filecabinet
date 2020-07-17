drop table if exists documents;
drop table if exists developers;
drop sequence if exists global_seq;

create sequence global_seq start with 1000;

create table developers (
    id integer primary key default nextval('global_seq'),
    name varchar not null
);
create unique index developers_unique_name_idx on developers (name);

create table documents (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    decimal_number varchar not null,
    inventory_number integer not null,
    stage varchar default null,
    developer_id integer default null,
    foreign key (developer_id) references developers (id)
);
create unique index documents_unique_decimal_number_idx on documents (decimal_number);
create unique index documents_unique_inventory_number_idx on documents (inventory_number);
