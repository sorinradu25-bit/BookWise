insert into users(email, role)
values ('demo@bookwise.local', 'USER')
on conflict (email) do nothing;