insert into pass_product (name, amount, period, chat_times, unit_study_times, pass_type)
values ('기간제 이용권', 55000, 30, null, null, 'SUBSCRIPTION'),
       ('소모성 질문권', 4000, null, 5, null, 'CHAT_CONSUMABLE'),
       ('소모성 단원 학습권', 0, null, 3, 1, 'UNIT_CONSUMABLE');

insert into member (email, name)
values ('hong@gmail.com', '홍길동');