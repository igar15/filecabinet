delete from applicabilities;
delete from document_external_dispatches;
delete from document_internal_dispatches;
delete from external_dispatches;
delete from internal_dispatches;
delete from document_change_notices;
delete from change_notices;
delete from documents;
delete from developers;
delete from companies;
alter sequence global_seq restart with 1000;

insert into developers (name, chief_name, description, workers_amount, is_developer, can_take_albums)
values ('KTK-40', 'KTK-40 chief name', null, null, true, true),
       ('NIO-6', 'NIO-6 chief name', 'Developers from NIO-6 are good folks', 24, true, true),
       ('NIO-8', 'NIO-8 chief name', null, 15, true, true),
       ('SKB-3', 'SKB-3 chief name', 'SKB-3 is a very good department. There are many talent people work here.', 43, true, true),
       ('OTD 49/3', 'Elkina R.M.', 'technical documents department', 21, false, true),
       ('OTD 33', 'OTD 33 chief name', 'OTD 33 makes great job for the organization!', 22, true, true);

insert into companies (name, city, street, building, zipcode, contact_person)
values ('NTC "NIEMI"', 'Moscow', 'Vereyskaya street', '41', '121357', 'Raev A.A.'),
       ('AO "MMZ"', 'Yoshkar-Ola', 'Suvorov street', '15', '424003', 'Bozhko M.V.'),
       ('AO "IEMZ"', 'Izhevsk', 'Pesochnaya street', '3', '426033', 'Visher S.K.');

insert into documents (name, decimal_number, inventory_number, receipt_date, status, form,
                       stage, sheets_amount, format, a4_amount, developer_id, original_holder_id)
values ('Стойка', 'БА6.151.128', 880572, '2003-01-30', 'ORIGINAL', 'ELECTRONIC', 'O1', null, null, null, 1000, 1006),
       ('Блок Д02-6М1Э Перечень элементов', 'ВУИА.465211.001ПЭ3', 63140, '2015-03-20', 'ORIGINAL', 'PAPER', 'O1', 28, 'A4', 28, 1005, 1006),
       ('Крышка', 'ЮПИЯ.301265.026', 884986, '2014-06-30', 'ORIGINAL', 'ELECTRONIC','O1', null, null, null, 1000, 1006),
       ('Крышка Сборочный чертеж', 'ЮПИЯ.301265.026СБ', 939986, '2014-06-30', 'ORIGINAL', 'PAPER', 'O1', 1, 'A1', 8, 1000, 1006),
       ('Пульт АРМ ОП СЭ01-7Э Схема электрическая принципиальная', 'ВУИА.468362.021Э3', 68199, '2018-09-15', 'ACC_COPY', 'ELECTRONIC', null, null, null, null, null, 1007),
       ('Панель ПСЭ25-22 Перечень элементов', 'ЮПИЯ.468151.031ПЭ3', 929213, '2012-11-18', 'ACC_COPY', 'ELECTRONIC', null, null, null, null, null, 1008);

insert into applicabilities (inner_id, outer_id)
values (1011, 1009),
       (1012, 1011),
       (1011, 1010);


insert into change_notices (name, change_code, issue_date, developer_id)
values ('ВУИА.СЭ.739', 5, '2018-05-24', 1000),
       ('ВУИА.СЭ.744', 8, '2018-06-17', 1000),
       ('ВУИА.ТК.133', 4, '2018-08-30', 1002),
       ('ВУИА.ТК.153', 2, '2019-04-24', 1002),
       ('ВУИА.ТК.156', 7, '2020-05-18', 1002),
       ('ВУИА.СО.134', 6, '2016-12-19', 1003),
       ('ВУИА.СО.135', 6, '2018-05-24', 1003),
       ('ВУИА.СО.136', 7, '2018-05-27', 1003);

insert into document_change_notices (document_id, change_notice_id, change)
values (1009, 1015, 1),
       (1009, 1016, 2),
       (1010, 1017, 1),
       (1014, 1017, 1),
       (1010, 1018, 2),
       (1014, 1018, 2),
       (1014, 1019, 3),
       (1011, 1020, 1),
       (1011, 1021, 2),
       (1011, 1022, 3);

insert into external_dispatches (waybill, dispatch_date, status, remark, letter_outgoing_number, company_id)
values ('wb-465', '2018-04-20', 'ACC_COPY', null, '49/SZ-123789', 1007),
       ('wb-467', '2018-04-15', 'DUPLICATE', 'simple remark', '49/SZ-1229', 1008),
       ('wb-546', '2019-06-20', 'ACC_COPY', 'true remark', '49/SZ-1289', 1007);

insert into document_external_dispatches (document_id, external_dispatch_id, is_active)
values (1009, 1023, true),
       (1009, 1024, true),
       (1010, 1025, true);

insert into internal_dispatches (waybill, dispatch_date, status, remark, stamp, developer_id,
                                 received_internal_date, internal_handler_name, internal_handler_phone_number, is_album, album_name)
values ('wb-531', '2017-04-15', 'ACC_COPY', null, 'I', 1000, '2019-02-15', 'Naumkin', '1-31-65', false, null),
       ('wb-532', '2018-04-20', 'ACC_COPY', 'simple remark', 'V', 1000, '2020-01-15', 'Naumkin', '1-31-65', true, 'ЮПИЯ.301265.026'),
       ('wb-556', '2019-06-20', 'ACC_COPY', 'true remark', 'I', 1000, '2020-04-18', 'Fatelnikova', '1-34-68', true, 'БА6.151.128');

insert into document_internal_dispatches (document_id,internal_dispatch_id)
values (1009, 1026),
       (1009, 1027),
       (1010, 1028);