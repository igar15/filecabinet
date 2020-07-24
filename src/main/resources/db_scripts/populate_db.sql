delete from document_change_notices;
delete from documents;
delete from change_notices;
delete from developers;
alter sequence global_seq restart with 1000;

insert into developers (name, chief_name, description, workers_amount)
values ('KTK-40', 'KTK-40 chief name', null, null),
       ('NIO-6', 'NIO-6 chief name', 'Developers from NIO-6 are good folks', '24'),
       ('NIO-8', 'NIO-8 chief name', null, '15'),
       ('SKB-3', 'SKB-3 chief name', 'SKB-3 is a very good department. There are many talent people work here.', '43'),
       ('OTD 25', 'OTD 25 chief name', null, '11'),
       ('OTD 33', 'OTD 33 chief name', 'OTD 33 makes great job for the organization!', '22');

insert into documents (name, decimal_number, inventory_number, stage, developer_id)
values ('Стойка', 'БА6.151.128', '880572', 'O1', '1000'),
       ('Блок Д02-6М1Э Перечень элементов', 'ВУИА.465211.001ПЭ3', '63140', null, '1003'),
       ('Крышка', 'ЮПИЯ.301265.026', '884986', 'O1', '1000'),
       ('Крышка Сборочный чертеж', 'ЮПИЯ.301265.026СБ', '939986', 'O1', '1000'),
       ('Пульт АРМ ОП СЭ01-7Э Схема электрическая принципиальная', 'ВУИА.468362.021Э3', '68199', null, '1002'),
       ('Панель ПСЭ25-22 Перечень элементов', 'ЮПИЯ.468151.031ПЭ3', '929213', null, '1004');

insert into change_notices (name, change_code, developer_id)
values ('ВУИА.СЭ.739', '5', 1000),
       ('ВУИА.СЭ.744', '8', 1000),
       ('ВУИА.ТК.133', '4', 1002),
       ('ВУИА.ТК.153', '2', 1002),
       ('ВУИА.ТК.156', '7', 1002),
       ('ВУИА.СО.134', '6', 1003),
       ('ВУИА.СО.135', '6', 1003),
       ('ВУИА.СО.136', '7', 1003);

insert into document_change_notices (document_id, change_notice_id)
values ('1006', '1012'),
       ('1006', '1013'),
       ('1007', '1014'),
       ('1011', '1014'),
       ('1007', '1015'),
       ('1011', '1015'),
       ('1011', '1016'),
       ('1008', '1017'),
       ('1008', '1018'),
       ('1008', '1019');