CREATE TABLE position (
  position_id BIGSERIAL PRIMARY KEY,
  shipping_list_id BIGINT REFERENCES shipping_list(shipping_list_id),
  product_id BIGINT REFERENCES product(product_id),
  price NUMERIC(17, 2),
  amount INT
)
