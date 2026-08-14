-- Dev-only seed account for exercising the ADMIN role (same pattern as
-- the demo@bookwise.local seed in V5/V6: fine for local dev, not for prod).
insert into users(email, role, password_hash)
values ('admin@bookwise.local', 'ADMIN', '$2y$10$z5InA9164i0IifetGI0vyOFGQQI6E.p70d.NWxIOEjVD7ZTVQ6Uie')
on conflict (email) do nothing;
