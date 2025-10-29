BEGIN;

-- First, ensure the user exists (insert if not present)
INSERT INTO users (id, created_at, updated_at, nickname, email, password)
VALUES ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW(), NOW(), 'nickname', 'em', 'pwd')
ON CONFLICT (id) DO UPDATE SET updated_at = NOW();

TRUNCATE TABLE notifications RESTART IDENTITY;

-- Finally, insert notifications
INSERT INTO notifications (id, confirmed, user_id, content, resource_type, resource_id, created_at, updated_at)
VALUES ('00000000-aaaa-bbbb-cccc-000000000001', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '1일 전 알림입니다.', 'interest',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
       ('00000000-aaaa-bbbb-cccc-000000000002', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '2일 전 알림입니다.', 'comment',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day'),
       ('00000000-aaaa-bbbb-cccc-000000000003', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '3일 전 알림입니다.', 'interest',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day'),
       ('00000000-aaaa-bbbb-cccc-000000000004', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '4일 전 알림입니다.', 'comment',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day'),
       ('00000000-aaaa-bbbb-cccc-000000000005', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '5일 전 알림입니다.', 'interest',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day'),
       ('00000000-aaaa-bbbb-cccc-000000000006', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '6일 전 알림입니다.', 'comment',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '6 day', NOW() - INTERVAL '6 day'),
       ('00000000-aaaa-bbbb-cccc-000000000007', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '7일 전 알림입니다.', 'interest',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '7 day', NOW() - INTERVAL '7 day'),
       ('00000000-aaaa-bbbb-cccc-000000000008', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '8일 전 알림입니다.', 'comment',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '8 day', NOW() - INTERVAL '8 day'),
       ('00000000-aaaa-bbbb-cccc-000000000009', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '9일 전 알림입니다.', 'interest',
        'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '9 day', NOW() - INTERVAL '9 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000a', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '10일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '10 day', NOW() - INTERVAL '10 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000b', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '11일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '11 day', NOW() - INTERVAL '11 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000c', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '12일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '12 day', NOW() - INTERVAL '12 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000d', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '13일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '13 day', NOW() - INTERVAL '13 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000e', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '14일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '14 day', NOW() - INTERVAL '14 day'),
       ('00000000-aaaa-bbbb-cccc-00000000000f', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '15일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '15 day', NOW() - INTERVAL '15 day'),
       ('00000000-aaaa-bbbb-cccc-000000000010', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '16일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '16 day', NOW() - INTERVAL '16 day'),
       ('00000000-aaaa-bbbb-cccc-000000000011', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '17일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '17 day', NOW() - INTERVAL '17 day'),
       ('00000000-aaaa-bbbb-cccc-000000000012', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '18일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '18 day', NOW() - INTERVAL '18 day'),
       ('00000000-aaaa-bbbb-cccc-000000000013', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '19일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '19 day', NOW() - INTERVAL '19 day'),
       ('00000000-aaaa-bbbb-cccc-000000000014', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '20일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '20 day', NOW() - INTERVAL '20 day'),
       ('00000000-aaaa-bbbb-cccc-000000000015', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '21일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '21 day', NOW() - INTERVAL '21 day'),
       ('00000000-aaaa-bbbb-cccc-000000000016', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '22일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '22 day', NOW() - INTERVAL '22 day'),
       ('00000000-aaaa-bbbb-cccc-000000000017', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '23일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '23 day', NOW() - INTERVAL '23 day'),
       ('00000000-aaaa-bbbb-cccc-000000000018', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '24일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '24 day', NOW() - INTERVAL '24 day'),
       ('00000000-aaaa-bbbb-cccc-000000000019', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '25일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '25 day', NOW() - INTERVAL '25 day'),
       ('00000000-aaaa-bbbb-cccc-00000000001a', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '26일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '26 day', NOW() - INTERVAL '26 day'),
       ('00000000-aaaa-bbbb-cccc-00000000001b', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '27일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '27 day', NOW() - INTERVAL '27 day'),
       ('00000000-aaaa-bbbb-cccc-00000000001c', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '28일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '28 day', NOW() - INTERVAL '28 day'),
       ('00000000-aaaa-bbbb-cccc-00000000001d', true, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '29일 전 알림입니다.',
        'interest', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '29 day', NOW() - INTERVAL '29 day'),
       ('00000000-aaaa-bbbb-cccc-00000000001e', false, 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', '30일 전 알림입니다.',
        'comment', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee', NOW() - INTERVAL '30 day', NOW() - INTERVAL '30 day');

COMMIT;