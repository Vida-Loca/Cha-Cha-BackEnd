INSERT INTO role VALUES (1, 'USER'), (2, 'ADMIN');
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (11, 'test1@o2.pl', true, '2001-01-01T10:22', 'test1', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'url1', 'Test1', 'testowy1', 1, false, false, false);
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (12, 'test2@o2.pl', true, '2001-01-02T10:33', 'test2', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'url2', 'Test2', 'changePass', 1, true, false, false);
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (13, 'admin1@o2.pl', true, '2001-01-03T10:33', 'admin1', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'url3', 'Admin1', 'admin1', 2, false, false, false);
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (14, 'toConfirm@o2.pl', false, '2001-01-03T10:33', 'confirm', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'url4', 'toConfirm', 'confirm', 1, false, false, false);


INSERT INTO address VALUES (1, 'TestCity', 'TestCountry', 10, 10, '5A', '14-240', 'TestStr');

INSERT INTO event (event_id, additional_information, currency, event_type, is_over, name, start_time, address_id) VALUES (1, 'info', 'PLN', 'PUBLIC', false, 'TestEvent', '2020-10-10T20:20', 1);
INSERT INTO event_user (is_admin, event_id, user_id) VALUES (true, 1, 11);

INSERT INTO event (event_id, additional_information, currency, event_type, is_over, name, start_time, address_id) VALUES (2, 'info1', 'PLN', 'PUBLIC', false, 'TestEvent1', '2020-10-10T20:21', 1);
INSERT INTO event_user (is_admin, event_id, user_id) VALUES (false, 2, 11);

INSERT INTO reset_password_token VALUES (1, '2020-10-10T20:21', 'valid', 11);
INSERT INTO reset_password_token VALUES (2, '2000-10-10T20:21', 'expired', 11);
INSERT INTO reset_password_token VALUES (3, '2020-10-10T20:21', 'invalid', 12);

INSERT INTO verification_token VALUES (1, '2020-10-10T20:21', 'valid', 14);
INSERT INTO verification_token VALUES (2, '2000-10-10T20:21', 'expired', 14);