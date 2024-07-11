--liquibase formatted sql
--changeset SkorikArtur:1 stripComments:true splitStatements:true

create table public.rooms
(
    id               uuid        default gen_random_uuid()
        constraint rooms_pk
            primary key,
    owner_id         uuid                      not null,
    code             varchar(4),
    max_participants integer,
    created_at       timestamptz default now() not null
);

comment
on column public.rooms.code is 'Security code for the room';
comment
on column public.rooms.owner_id is 'Play hub user id who created the room';
comment
on column public.rooms.created_at is 'Timestamp of creation the room';

