insert into subscription_product (name, amount, period, chat_times, unit_study_times, subscription_type)
values ('기간제 구독권', 55000, 30, null, null, 'PERIOD'),
       ('유료 일회용 구독권', 4000, null, 5, null, 'PAID_SINGLE'),
       ('무료 일회용 구독권', 0, null, 3, 1, 'FREE_SINGLE');

insert into member (email, name)
values ('hong@gmail.com', '홍길동');