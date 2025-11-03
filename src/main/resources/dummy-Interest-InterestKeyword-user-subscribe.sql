BEGIN;

-- 기존 데이터 초기화 (순서 중요)
TRUNCATE TABLE interest_keywords RESTART IDENTITY CASCADE;
TRUNCATE TABLE subscriptions RESTART IDENTITY CASCADE;
TRUNCATE TABLE interests RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- users 더미 데이터 (created_at = NOW() - 2일, deleted_at = NOW() - 1일)
INSERT INTO users (id, email, nickname, password, deleted_at, created_at)
VALUES
    ('aaaaaaaa-1111-1111-1111-111111111111', 'user1@example.com', '사용자1', '$2a$10$dummyhashedpassword1', null, NOW() - INTERVAL '2 day'),
    ('aaaaaaaa-2222-2222-2222-222222222222', 'user2@example.com', '사용자2', '$2a$10$dummyhashedpassword2', null, NOW() - INTERVAL '2 day'),
    ('aaaaaaaa-3333-3333-3333-333333333333', 'user3@example.com', '사용자3', '$2a$10$dummyhashedpassword3', null, NOW() - INTERVAL '2 day'),
    ('aaaaaaaa-4444-4444-4444-444444444444', 'user4@example.com', '사용자4', '$2a$10$dummyhashedpassword4', null, NOW() - INTERVAL '2 day'),
    ('aaaaaaaa-5555-5555-5555-555555555555', 'delete1@example.com', '사용자삭제1', '$2a$10$dummyhashedpassword3', date_trunc('day', NOW()) - INTERVAL '1 day', NOW() - INTERVAL '4 day'),
    ('aaaaaaaa-6666-6666-6666-666666666666', 'delete2@example.com', '사용자삭제2', '$2a$10$dummyhashedpassword4', date_trunc('day', NOW()) - INTERVAL '1 day', NOW() - INTERVAL '5 day')
;


-- interests (고정 UUID)
INSERT INTO interests (id, name, created_at)
VALUES ('11111111-aaaa-bbbb-cccc-111111111111', '주식', NOW()),
       ('22222222-aaaa-bbbb-cccc-222222222222', '부동산', NOW()),
       ('33333333-aaaa-bbbb-cccc-333333333333', 'AI', NOW()),
       ('44444444-aaaa-bbbb-cccc-444444444444', '스포츠', NOW()),
       ('55555555-aaaa-bbbb-cccc-555555555555', '정치', NOW()),
       ('66666666-aaaa-bbbb-cccc-666666666666', '자동차', NOW());

-- interest_keywords
INSERT INTO interest_keywords (id, name, interest_id)
VALUES ('8a9c2a10-1d7e-4f7e-9a01-000000000001', '삼성전자', '11111111-aaaa-bbbb-cccc-111111111111'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000002', '코스피', '11111111-aaaa-bbbb-cccc-111111111111'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000003', 'ETF', '11111111-aaaa-bbbb-cccc-111111111111'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000004', '환율', '11111111-aaaa-bbbb-cccc-111111111111'),

       ('8a9c2a10-1d7e-4f7e-9a01-000000000005', '아파트', '22222222-aaaa-bbbb-cccc-222222222222'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000006', '전세', '22222222-aaaa-bbbb-cccc-222222222222'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000007', '분양', '22222222-aaaa-bbbb-cccc-222222222222'),

       ('8a9c2a10-1d7e-4f7e-9a01-000000000008', '딥러닝', '33333333-aaaa-bbbb-cccc-333333333333'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000009', '오픈AI', '33333333-aaaa-bbbb-cccc-333333333333'),
       ('8a9c2a10-1d7e-4f7e-9a01-00000000000a', '로봇', '33333333-aaaa-bbbb-cccc-333333333333'),
       ('8a9c2a10-1d7e-4f7e-9a01-00000000000b', '챗GPT', '33333333-aaaa-bbbb-cccc-333333333333'),

       ('8a9c2a10-1d7e-4f7e-9a01-00000000000c', 'KBO', '44444444-aaaa-bbbb-cccc-444444444444'),
       ('8a9c2a10-1d7e-4f7e-9a01-00000000000d', '야구', '44444444-aaaa-bbbb-cccc-444444444444'),
       ('8a9c2a10-1d7e-4f7e-9a01-00000000000e', '프리미어리그', '44444444-aaaa-bbbb-cccc-444444444444'),
       ('8a9c2a10-1d7e-4f7e-9a01-00000000000f', '손흥민', '44444444-aaaa-bbbb-cccc-444444444444'),

       ('8a9c2a10-1d7e-4f7e-9a01-000000000010', '국회', '55555555-aaaa-bbbb-cccc-555555555555'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000011', '대선', '55555555-aaaa-bbbb-cccc-555555555555'),

       ('8a9c2a10-1d7e-4f7e-9a01-000000000012', '전기차', '66666666-aaaa-bbbb-cccc-666666666666'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000013', '자율주행', '66666666-aaaa-bbbb-cccc-666666666666'),
       ('8a9c2a10-1d7e-4f7e-9a01-000000000014', '배터리', '66666666-aaaa-bbbb-cccc-666666666666');

-- subscriptions
INSERT INTO subscriptions (id, user_id, interest_id, created_at)
VALUES
    -- 주식 구독자 3명
    ('bbbbbbbb-0000-0000-0000-000000000001', 'aaaaaaaa-1111-1111-1111-111111111111',
     '11111111-aaaa-bbbb-cccc-111111111111', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000002', 'aaaaaaaa-2222-2222-2222-222222222222',
     '11111111-aaaa-bbbb-cccc-111111111111', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000003', 'aaaaaaaa-3333-3333-3333-333333333333',
     '11111111-aaaa-bbbb-cccc-111111111111', NOW()),

    -- 부동산 구독자 2명
    ('bbbbbbbb-0000-0000-0000-000000000004', 'aaaaaaaa-1111-1111-1111-111111111111',
     '22222222-aaaa-bbbb-cccc-222222222222', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000005', 'aaaaaaaa-2222-2222-2222-222222222222',
     '22222222-aaaa-bbbb-cccc-222222222222', NOW()),

    -- AI 구독자 4명
    ('bbbbbbbb-0000-0000-0000-000000000006', 'aaaaaaaa-1111-1111-1111-111111111111',
        '33333333-aaaa-bbbb-cccc-333333333333', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000007', 'aaaaaaaa-2222-2222-2222-222222222222',
     '33333333-aaaa-bbbb-cccc-333333333333', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000008', 'aaaaaaaa-3333-3333-3333-333333333333',
     '33333333-aaaa-bbbb-cccc-333333333333', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000009', 'aaaaaaaa-4444-4444-4444-444444444444',
     '33333333-aaaa-bbbb-cccc-333333333333', NOW()),

    -- 스포츠 구독자 3명
    ('bbbbbbbb-0000-0000-0000-00000000000a', 'aaaaaaaa-2222-2222-2222-222222222222',
     '44444444-aaaa-bbbb-cccc-444444444444', NOW()),
    ('bbbbbbbb-0000-0000-0000-00000000000b', 'aaaaaaaa-3333-3333-3333-333333333333',
     '44444444-aaaa-bbbb-cccc-444444444444', NOW()),
    ('bbbbbbbb-0000-0000-0000-00000000000c', 'aaaaaaaa-4444-4444-4444-444444444444',
     '44444444-aaaa-bbbb-cccc-444444444444', NOW()),

    -- 정치 구독자 2명
    ('bbbbbbbb-0000-0000-0000-00000000000d', 'aaaaaaaa-1111-1111-1111-111111111111',
     '55555555-aaaa-bbbb-cccc-555555555555', NOW()),
    ('bbbbbbbb-0000-0000-0000-00000000000e', 'aaaaaaaa-4444-4444-4444-444444444444',
     '55555555-aaaa-bbbb-cccc-555555555555', NOW()),

    -- 자동차 구독자 3명
    ('bbbbbbbb-0000-0000-0000-00000000000f', 'aaaaaaaa-1111-1111-1111-111111111111',
     '66666666-aaaa-bbbb-cccc-666666666666', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000010', 'aaaaaaaa-3333-3333-3333-333333333333',
     '66666666-aaaa-bbbb-cccc-666666666666', NOW()),
    ('bbbbbbbb-0000-0000-0000-000000000011', 'aaaaaaaa-4444-4444-4444-444444444444',
     '66666666-aaaa-bbbb-cccc-666666666666', NOW());

COMMIT;