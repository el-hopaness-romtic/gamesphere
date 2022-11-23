CREATE TABLE shipping_list (
  shipping_list_id BIGSERIAL PRIMARY KEY,
  shipping_date TIMESTAMP,
  organization_id BIGINT references organization(organization_id)
)
