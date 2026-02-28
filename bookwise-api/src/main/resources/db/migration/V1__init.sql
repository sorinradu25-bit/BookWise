CREATE TABLE IF NOT EXISTS providers (
  id BIGSERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS availability_slots (
  id BIGSERIAL PRIMARY KEY,
  provider_id BIGINT NOT NULL,
  start_at TIMESTAMPTZ NOT NULL,
  end_at   TIMESTAMPTZ NOT NULL,
  status   VARCHAR(32) NOT NULL,
  version  BIGINT NOT NULL DEFAULT 0,

  CONSTRAINT fk_availability_provider
    FOREIGN KEY (provider_id) REFERENCES providers(id)
);

CREATE INDEX IF NOT EXISTS idx_slots_provider_start
  ON availability_slots(provider_id, start_at);