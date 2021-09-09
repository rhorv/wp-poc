CREATE TABLE bill (reference char(8) NOT NULL, merchant_id char(36) NOT NULL, status varchar(16) NOT NULL, PRIMARY KEY (reference, merchant_id));
CREATE TABLE payment (id char(36) NOT NULL UNIQUE PRIMARY KEY, reference char(8) NOT NULL, merchant_id char(36) NOT NULL, charge_amount integer NOT NULL, charge_currency char(3) NOT NULL, CONSTRAINT fk_bill FOREIGN KEY(reference, merchant_id) REFERENCES bill(reference, merchant_id));