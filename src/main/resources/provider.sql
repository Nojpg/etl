DROP TABLE IF EXISTS provider ;
CREATE TABLE provider(
  account_id BIGINT IDENTITY PRIMARY KEY NOT NULL,
  account_name VARCHAR(60),
  traffic_volume VARCHAR(60),
  address VARCHAR(200)
)