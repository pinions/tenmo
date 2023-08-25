BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);



INSERT INTO tenmo_user (username, password_hash)
VALUES ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy'),
       ('nicholas', '$2a$10$XYwSwRxxkUxGcsKyozcwEur5UXJ2nyXLki.BORll9jsGxZEcy3CXW'),
       ('nix', '$2a$10$7CcedCBqk9hvQAHO5CnmI.UkolotAD1yCXe.3PjFn/zGHUkLsLTre'),
       ('zach', '$2a$10$pXkUuMMRbKFmmBiY8GMQhedq.h.ndUloFRifAAApY1JRxIH3WisWC'),
       ('caroline', '$2a$10$3g/UEmNYkqxHXexG9ue9Jupkny3A4L1q9u9Hc1laxNiKZyn4ue7Ei'),
       ('viviana', '$2a$10$Zulu59gjirAZeS2XDtMRwOE5FNjvbuOMd5DL35Kqxa/5qa1JOIb0e'),
       ('michel', '$2a$10$cwX3e7zclBT4SdYJ9680n.Rd7RCUcPW6Xr1VS3cABMAAs5RfpHg6m'),
       ('adam', '$2a$10$GcQkUuO33aRgqIxJzxRilO3JnoKEVHaDXYRMwbPTxGKGRLLBJ05Cu'),
       ('charles', '$2a$10$NakCoyQY3jSwMzmI4eVrAe0wlT0ogbgKq0LOvOZrsL5bbrML14qOi');


INSERT INTO account (user_id, balance)
VALUES (1001, 1000),
       (1002, 2000),
       (1003, 400),
       (1004, 599),
       (1005, 5),
       (1006, 50),
       (1007, 1000),
       (1008, 800),
       (1009, 70),
       (1010, 1200);


CREATE SEQUENCE seq_transfer_id
INCREMENT BY 1
START WITH 3001
NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	transfer_amount int NOT NULL,
	sender_username varchar(50) NOT NULL,
	receiver_username varchar(50) NOT NULL,
	CONSTRAINT PK_transfer_id PRIMARY KEY (transfer_id),
	CONSTRAINT FK_sender_username FOREIGN KEY (sender_username) REFERENCES tenmo_user (username)
);


INSERT INTO transfer (transfer_amount, sender_username, receiver_username)
VALUES (1000, 'charles', 'nicholas'),
       (500, 'nix', 'nicholas'),
       (600, 'michel', 'zach'),
       (900, 'viviana', 'zach'),
       (50, 'adam', 'nicholas'),
       (80, 'user', 'adam'),
       (5, 'caroline', 'adam'),
       (9, 'nicholas', 'nix');


COMMIT;
