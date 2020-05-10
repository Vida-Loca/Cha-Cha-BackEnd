
INSERT INTO role VALUES (1, 'USER'), (2, 'ADMIN');
/*users - id, canChangePass, email, enabled, isBanned, isWarned, joined, name, password, picUrl, surname, username, roleId */
INSERT INTO userr VALUES (10, false, 'test10@o2.pl', true, false, false, '2001-01-01T10:22', 'Name10', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl10', 'Surname10', 'testowy1', 1);
INSERT INTO userr VALUES (11, true, 'test11@o2.pl', true, false, false, '2001-01-02T10:33', 'Name11', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl11', 'Surname11', 'changePass', 1);
INSERT INTO userr VALUES (12, false, 'admin1@o2.pl', true, false, false, '2001-01-03T10:33', 'Name12', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl12', 'Surname12', 'admin1', 2);
INSERT INTO userr VALUES (13, false, 'toConfirm@o2.pl', false, false, false, '2001-01-03T10:33', 'Name13', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl13', 'Surname13', 'confirm', 1);
INSERT INTO userr VALUES (14, false, 'test14@o2.pl', true, false, false, '2001-01-01T10:22', 'Name14', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl14', 'Surname14', 'testowy2', 1);
INSERT INTO userr VALUES (15, false, 'test15@o2.pl', true, false, false, '2001-01-01T10:22', 'Name15', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'picUrl15', 'Surname15', 'testowy3', 1);


INSERT INTO reset_password_token VALUES (10, '2020-10-10T20:21', 'valid', 10);
INSERT INTO reset_password_token VALUES (11, '2000-10-10T20:21', 'expired', 10);
INSERT INTO reset_password_token VALUES (12, '2020-10-10T20:21', 'invalid', 12);

INSERT INTO verification_token VALUES (10, '2020-10-10T20:21', 'valid', 13);
INSERT INTO verification_token VALUES (11, '2000-10-10T20:21', 'expired', 13);

/* address - id, city, country, latitude, longitude, number, postcode, street */
INSERT INTO address VALUES (10, 'City10', 'Country10', 10, 10, '10', '10-240', 'Street10');
INSERT INTO address VALUES (11, 'City11', 'Country11', 11, 11, '11', '11-240', 'Street11');

INSERT INTO product_category VALUES (10, 'FOOD');
INSERT INTO product_category VALUES (11, 'DRINK');

/* event - id, info, currency, eventType, isOver, name, startTime, addressId
   event_user - id, isAdmin, eventId, userId */
INSERT INTO event VALUES (10, 'info10', 'PLN', 'PUBLIC', false, 'TestEvent10', '2020-10-10T20:20', 10);
INSERT INTO event_user VALUES (10, true, 10, 10);
INSERT INTO event_user VALUES (13, false, 10, 14);
INSERT INTO event_user VALUES (14, false, 10, 15);
/* product - id, name, price, quantity, eventUserId, productCategoryId */
INSERT INTO product VALUES (10, 'FoodEvent10', 1, 2, 10, 10);
INSERT INTO product VALUES (12, 'DrinkEvent10', 3, 3, 10, 11);
INSERT INTO product VALUES (13, 'Food1Event10', 2.5, 1, 13, 10);
/* event - id, info, currency, eventType, isOver, name, startTime, addressId
   event_user - id, isAdmin, eventId, userId */
INSERT INTO event VALUES (11, 'info11', 'PLN', 'PUBLIC', false, 'TestEvent11', '2020-10-10T20:21', 11);
INSERT INTO event_user VALUES (11, false, 11, 10);
INSERT INTO event_user VALUES (12, true, 11, 11);
/* product - id, name, price, quantity, eventUserId, productCategoryId */
INSERT INTO product VALUES (11, 'FoodEvent11', 3.2, 2, 12, 10);
INSERT INTO product VALUES (14, 'Food1Event11', 13.1, 1, 11, 10);


