INSERT INTO rooms (id, owner_id, code)
VALUES ('638f2195-ee04-48cd-ada0-90f6711f0cad', 'b2b27a52-24bd-41df-8037-6ea9150d7945', '5555');
insert into room_participants (room_id, participant_id)
values ('638f2195-ee04-48cd-ada0-90f6711f0cad', 'b2b27a52-24bd-41df-8037-6ea9150d7945');

INSERT INTO rooms (id, owner_id, code, max_participants)
VALUES ('ec92ac5d-536b-486a-8e35-21eeca1e877b', '5012b1d8-01ec-454e-adc1-5fa0d17c58ea', null, 2);
insert into room_participants (room_id, participant_id)
values
    ('ec92ac5d-536b-486a-8e35-21eeca1e877b', 'ec92ac5d-536b-486a-8e35-21eeca1e877b'),
    ('ec92ac5d-536b-486a-8e35-21eeca1e877b', '60941551-ad14-4c8f-8101-b84438ac0f69');

INSERT INTO rooms (id, owner_id, code, max_participants)
VALUES ('a666cd6b-119f-4cd6-9ffe-3c820496cbeb', 'd697283f-7e91-4023-9e71-ac67fafaf4b5', null, 50);
insert into room_participants (room_id, participant_id)
values
    ('a666cd6b-119f-4cd6-9ffe-3c820496cbeb', 'd697283f-7e91-4023-9e71-ac67fafaf4b5'),
    ('a666cd6b-119f-4cd6-9ffe-3c820496cbeb', '590f1642-1959-4185-b602-f6b3fcdf1fd5');

INSERT INTO rooms (id, owner_id, code, max_participants)
VALUES ('364c0c8b-2c64-4de7-a3cf-adc5d7a4df11', '590f1642-1959-4185-b602-f6b3fcdf1fd5', null, 50);
insert into room_participants (room_id, participant_id)
values
    ('364c0c8b-2c64-4de7-a3cf-adc5d7a4df11', '590f1642-1959-4185-b602-f6b3fcdf1fd5'),
    ('364c0c8b-2c64-4de7-a3cf-adc5d7a4df11', '6bbf611e-31a0-4b20-abcf-f26a24d582db');