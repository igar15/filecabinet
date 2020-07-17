delete from documents;
delete from developers;
alter sequence global_seq restart with 1000;

insert into developers (name)
values ('KTK-40'),
       ('NIO-6'),
       ('NIO-8'),
       ('SKB-3'),
       ('OTD 25'),
       ('OTD 33');

insert into documents (name, decimal_number, inventory_number, stage, developer_id)
values ('Стойка', 'БА6.151.128', '880572', 'O1', '1000'),
       ('Блок Д02-6М1Э Перечень элементов', 'ВУИА.465211.001ПЭ3', '63140', null, '1003'),
       ('Крышка', 'ЮПИЯ.301265.026', '884986', 'O1', '1000'),
       ('Крышка Сборочный чертеж', 'ЮПИЯ.301265.026СБ', '939986', 'O1', '1000'),
       ('Пульт АРМ ОП СЭ01-7Э Схема электрическая принципиальная', 'ВУИА.468362.021Э3', '68199', null, '1002'),
       ('Панель ПСЭ25-22 Перечень элементов', 'ЮПИЯ.468151.031ПЭ3', '929213', null, '1004');