-- users (for domain.User)
create table users (
  id bigserial primary key,
  email varchar(255) not null unique,
  role varchar(50) not null
);

-- app_user (for domain.AppUser, used by BookingService BasicAuth)
create table app_user (
  id bigserial primary key,
  email varchar(255) not null unique,
  role varchar(50) not null
);

-- bookings
create table bookings (
  id bigserial primary key,
  slot_id bigint not null references availability_slots(id),
  user_id bigint not null references users(id),
  status varchar(50) not null,
  idempotency_key varchar(100) not null,
  request_hash varchar(64) null,
  created_at timestamptz not null default now(),
  version bigint not null default 0,

  constraint uq_booking_slot unique (slot_id),
  constraint uq_booking_idem unique (user_id, idempotency_key)
);

create index ix_booking_user_created on bookings(user_id, created_at desc);
create index ix_booking_slot on bookings(slot_id);