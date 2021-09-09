CREATE TABLE merchant (id char(36) NOT NULL UNIQUE PRIMARY KEY, tariff varchar(16) NOT NULL);
CREATE TABLE blended_merchant (id char(36) NOT NULL UNIQUE, charge_percent float NOT NULL, fixed_charge_amount integer NOT NULL, fixed_charge_currency char(3) NOT NULL, CONSTRAINT fk_merchant FOREIGN KEY(id) REFERENCES merchant(id));
CREATE TABLE passthrough_merchant (id char(36) NOT NULL UNIQUE, charge_percent float NOT NULL, CONSTRAINT fk_merchant FOREIGN KEY(id) REFERENCES merchant(id));
