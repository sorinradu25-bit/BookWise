insert into providers(name) values ('Demo Provider');

-- slots: azi + 2 ore, 3 ore, 4 ore
insert into availability_slots(provider_id, start_at, end_at, status, version)
values
  (1, now() + interval '2 hours', now() + interval '2 hours 30 minutes', 'AVAILABLE', 0),
  (1, now() + interval '3 hours', now() + interval '3 hours 30 minutes', 'AVAILABLE', 0),
  (1, now() + interval '4 hours', now() + interval '4 hours 30 minutes', 'AVAILABLE', 0);