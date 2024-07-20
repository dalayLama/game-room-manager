--liquibase formatted sql
--changeset SkorikArtur:2 stripComments:true splitStatements:true

create table public.room_participants
(
    id             bigserial
        constraint room_participants_pk
            primary key,
    room_id        uuid                      not null
        constraint room_participants_rooms_id_fk
            references public.rooms
            on update restrict on delete restrict,
    participant_id uuid                      not null,
    added_at       timestamptz default now() not null
);

comment on column public.room_participants.participant_id is 'Playhub user id';

comment on column public.room_participants.added_at is 'Timestamp when participant was added to the room';

create unique index room_participant_uindex
    on public.room_participants (room_id, participant_id);

