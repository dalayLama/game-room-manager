--liquibase formatted sql
--changeset SkorikArtur:2 stripComments:true splitStatements:true

create table public.room_participants
(
    room_id        uuid                      not null
        constraint participants_rooms_id_fk
            references public.rooms (id)
            on update restrict on delete restrict,
    participant_id uuid                      not null,
    added_at       timestamptz default now() not null,
    constraint room_participants_pk
        primary key (room_id, participant_id)
);

comment on column public.room_participants.room_id is 'Playhub user id';

comment on column public.room_participants.added_at is 'Timestamp when participant was added to the room';