drop table if exists applicabilities;
drop table if exists document_internal_dispatches;
drop table if exists document_external_dispatches;
drop table if exists external_dispatches;
drop table if exists internal_dispatches;
drop table if exists document_change_notices;
drop table if exists change_notices;
drop table if exists documents;
drop table if exists developers;
drop table if exists companies;
drop sequence if exists global_seq;

create sequence global_seq start with 1000;

create table developers (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    chief_name varchar not null,
    description varchar default null,
    workers_amount integer default null,
    is_developer boolean not null,
    can_take_albums boolean not null
);
create unique index developers_unique_name_idx on developers (name);

create table companies (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    city varchar not null,
    street varchar not null,
    building varchar not null,
    zipcode varchar not null,
    contact_person varchar default null
);
create unique index companies_unique_name_city_street_building_idx on companies (name, city, street, building);

create table documents (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    decimal_number varchar not null,
    inventory_number integer not null,
    receipt_date date not null,
    status varchar not null,
    form varchar not null,
    change_number integer default null,
    stage varchar default null,
    sheets_amount integer default null,
    format varchar default null,
    a4_amount integer default null,
    developer_id integer default null,
    original_holder_id integer default null,
    foreign key (developer_id) references developers (id),
    foreign key (original_holder_id) references companies (id)
);
create unique index documents_unique_decimal_number_idx on documents (decimal_number);
create unique index documents_unique_inventory_number_idx on documents (inventory_number);

create table applicabilities (
    inner_id integer not null,
    outer_id integer not null,
    primary key (inner_id, outer_id),
    foreign key (inner_id) references documents (id) on delete cascade,
    foreign key (outer_id) references documents (id) on delete cascade
);

create table change_notices (
    id integer primary key default nextval('global_seq'),
    name varchar not null,
    change_code integer not null,
    issue_date date not null,
    developer_id integer default null,
    foreign key (developer_id) references developers (id)
);
create unique index change_notices_name_idx on change_notices (name);

create table document_change_notices (
    document_id integer not null,
    change_notice_id integer not null,
    change integer default null,
    primary key(document_id, change_notice_id),
    foreign key (document_id) references documents (id),
    foreign key (change_notice_id) references change_notices (id)
);

create table external_dispatches (
    id integer primary key default nextval('global_seq'),
    waybill varchar not null,
    dispatch_date date not null,
    status varchar not null,
    remark varchar default null,
    letter_outgoing_number varchar not null,
    company_id integer not null,
    foreign key (company_id) references companies (id) on delete cascade
);
create unique index external_dispatches_waybill_dispatch_date_outgoing_number_idx on external_dispatches (waybill, dispatch_date, letter_outgoing_number);

create table document_external_dispatches (
    document_id integer not null,
    external_dispatch_id integer not null,
    primary key (document_id, external_dispatch_id),
    foreign key (document_id) references documents(id) on delete cascade,
    foreign key (external_dispatch_id) references external_dispatches (id)
);

create table internal_dispatches (
    id integer primary key default nextval('global_seq'),
    waybill varchar not null,
    dispatch_date date not null,
    status varchar not null,
    remark varchar default null,
    stamp varchar default null,
    developer_id integer not null,
    received_internal_date date not null,
    internal_handler_name varchar not null,
    internal_handler_phone_number varchar not null,
    is_album boolean not null,
    album_name varchar default null,
    foreign key (developer_id) references developers (id) on delete cascade
);
create unique index internal_dispatches_stamp_album_name_idx on internal_dispatches (stamp, album_name);
create unique index internal_dispatches_waybill_dispatch_date_idx on internal_dispatches (waybill, dispatch_date);

create table document_internal_dispatches (
    document_id integer not null,
    internal_dispatch_id integer not null,
    primary key (document_id, internal_dispatch_id),
    foreign key (document_id) references documents(id) on delete cascade,
    foreign key (internal_dispatch_id) references internal_dispatches (id)
);

