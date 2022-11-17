CREATE TABLE organization (
  organization_id BIGSERIAL PRIMARY KEY,
  inn VARCHAR(12),
  name VARCHAR(100),
  account_number CHAR(20)
)
