set search_path to otus_highload;

insert into otus_highload.person (id, first_name, second_name, birthdate, biography, city, created_at, updated_at)
values ('370c779a-3570-49e4-b066-34f1c06f494d', 'Леонардо', 'Ди Каприо', '2010-01-01', 'text text 1', 'Владивосток', now(), null),
       ('19bc1d82-b91f-4759-a62a-c8d199e29358', 'Леонель', 'Месси', '2011-02-01', 'text text 2', 'Москва', now(), null),
       ('abbdd14d-9dfb-434d-8696-fbffa0d52d5d', 'Леопольд', 'Подлый Трус', '2012-03-01', 'text text 3', 'Воронеж', now(), null),
       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', 'Вакула', 'Кузнец', '2013-04-01', 'text text 4', 'Липецк', now(), null),
       ('458fda80-8253-4da3-b4a8-83826b667931', 'Светка', 'Соколова', '2014-05-01', 'text text 5', 'Санкт-Петербург', now(), null),
       ('054bfb6b-3540-433b-a2c4-480612487285', 'Мамкин', 'Нагибатор', '2015-06-01', 'text text 6', 'Петропавловск-Камчатский', now(), null),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'Леди', 'Гага', '2016-07-01', 'text text 7', 'Урюпинск', now(), null);

insert into otus_highload.friend_request(user_id, friend_id)
VALUES ('370c779a-3570-49e4-b066-34f1c06f494d', '19bc1d82-b91f-4759-a62a-c8d199e29358'),
       ('370c779a-3570-49e4-b066-34f1c06f494d', '6bf4f55a-a1af-4fe2-a895-a14abca5a5e7'),
       ('370c779a-3570-49e4-b066-34f1c06f494d', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('19bc1d82-b91f-4759-a62a-c8d199e29358', '370c779a-3570-49e4-b066-34f1c06f494d'),
       ('19bc1d82-b91f-4759-a62a-c8d199e29358', '6bf4f55a-a1af-4fe2-a895-a14abca5a5e7'),
       ('19bc1d82-b91f-4759-a62a-c8d199e29358', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('abbdd14d-9dfb-434d-8696-fbffa0d52d5d', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', '370c779a-3570-49e4-b066-34f1c06f494d'),
       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', '19bc1d82-b91f-4759-a62a-c8d199e29358'),
       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('458fda80-8253-4da3-b4a8-83826b667931', '054bfb6b-3540-433b-a2c4-480612487285'),
       ('458fda80-8253-4da3-b4a8-83826b667931', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('054bfb6b-3540-433b-a2c4-480612487285', '458fda80-8253-4da3-b4a8-83826b667931'),
       ('054bfb6b-3540-433b-a2c4-480612487285', '10775498-ae09-4e13-a2ff-f7c1e190043f'),

       ('10775498-ae09-4e13-a2ff-f7c1e190043f', '370c779a-3570-49e4-b066-34f1c06f494d'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', '19bc1d82-b91f-4759-a62a-c8d199e29358'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'abbdd14d-9dfb-434d-8696-fbffa0d52d5d'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', '6bf4f55a-a1af-4fe2-a895-a14abca5a5e7'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', '458fda80-8253-4da3-b4a8-83826b667931'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', '054bfb6b-3540-433b-a2c4-480612487285');

insert into otus_highload.post(from_user, text)
VALUES ('370c779a-3570-49e4-b066-34f1c06f494d', 'text 1'),
       ('370c779a-3570-49e4-b066-34f1c06f494d', 'text 2'),
       ('370c779a-3570-49e4-b066-34f1c06f494d', 'text 3'),

       ('19bc1d82-b91f-4759-a62a-c8d199e29358', 'text 4'),
       ('19bc1d82-b91f-4759-a62a-c8d199e29358', 'text 5'),
       ('19bc1d82-b91f-4759-a62a-c8d199e29358', 'text 6'),

       ('abbdd14d-9dfb-434d-8696-fbffa0d52d5d', 'text 7'),
       ('abbdd14d-9dfb-434d-8696-fbffa0d52d5d', 'text 8'),
       ('abbdd14d-9dfb-434d-8696-fbffa0d52d5d', 'text 9'),

       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', 'text 10'),
       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', 'text 11'),
       ('6bf4f55a-a1af-4fe2-a895-a14abca5a5e7', 'text 12'),

       ('458fda80-8253-4da3-b4a8-83826b667931', 'text 13'),
       ('458fda80-8253-4da3-b4a8-83826b667931', 'text 14'),
       ('458fda80-8253-4da3-b4a8-83826b667931', 'text 15'),

       ('054bfb6b-3540-433b-a2c4-480612487285', 'text 16'),
       ('054bfb6b-3540-433b-a2c4-480612487285', 'text 17'),
       ('054bfb6b-3540-433b-a2c4-480612487285', 'text 18'),

       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 119'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 120'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 121'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 122'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 123'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 124'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 125'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 126'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 127'),
       ('10775498-ae09-4e13-a2ff-f7c1e190043f', 'text 128');