drop table if exists document_change_notices;
drop table if exists change_notices;
drop table if exists documents;
drop table if exists developers;
drop sequence if exists global_seq;

create sequence global_seq start with 1000;

create table developers (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    chief_name varchar not null,
    description varchar default null,
    workers_amount integer default null
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

create table change_notices (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    change_code integer not null,
    developer_id integer default null,
    foreign key (developer_id) references developers (id)
);
create unique index change_notices_name_idx on change_notices (name);

create table document_change_notices (
    document_id integer not null,
    change_notice_id integer not null,
    primary key(document_id, change_notice_id),
    foreign key (document_id) references documents (id),
    foreign key (change_notice_id) references change_notices (id)
);
