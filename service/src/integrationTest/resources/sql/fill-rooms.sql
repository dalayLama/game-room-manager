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