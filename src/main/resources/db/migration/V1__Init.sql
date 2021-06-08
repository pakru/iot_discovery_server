CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table controllers
(
    id          UUID
        CONSTRAINT currency_pkey PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT NULL,
    mac_address macaddr not null,
    external_ip inet not null

);
