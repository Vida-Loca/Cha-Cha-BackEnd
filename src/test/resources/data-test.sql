INSERT INTO role VALUES (1, 'USER'), (2, 'ADMIN');
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (1, 'test1@o2.pl', true, '2001-01-01T10:22', 'test1', '$2a$10$bMm8ygL4/mDV2Oevx6v4z.FLNDWdjlsajSHg7oAT.BcFRNYYeFZD2', 'url1', 'Test1', 'testowy1', 1, false, false, false);
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (2, 'test2@o2.pl', true, '2001-01-02T10:33', 'test2', '$2b$10$deQsiF09Y30FqFPP2EOd1OQBnLbeuynYD1EnmrRoCUhfLmt.XpYgG', 'url2', 'Test2', 'testowy2', 1, false, false, false);
INSERT INTO userr (user_id, email, enabled, joined, name, password, pic_url, surname, username, role_id, can_change_pass, is_banned, is_warned)  VALUES (3, 'test3@o2.pl', true, '2001-01-03T10:33', 'test3', '$2b$10$deQsiF09Y30FqFPP2EOd1OQBnLbeuynYD1EnmrRoCUhfLmt.XpYgG', 'url3', 'Test3', 'testowy3', 1, false, false, false);

INSERT INTO address VALUES (1, 'TestCity', 'TestCountry', 10, 10, '5A', '14-240', 'TestStr');

INSERT INTO event (event_id, additional_information, currency, event_type, is_over, name, start_time, address_id) VALUES (1, 'info', 'PLN', 'PUBLIC', false, 'TestEvent', '2020-10-10T20:20', 1);
INSERT INTO event_user (is_admin, event_id, user_id) VALUES (true, 1, 1);

INSERT INTO event (event_id, additional_information, currency, event_type, is_over, name, start_time, address_id) VALUES (2, 'info1', 'PLN', 'PUBLIC', false, 'TestEvent1', '2020-10-10T20:21', 1);
INSERT INTO event_user (is_admin, event_id, user_id) VALUES (false, 2, 1);