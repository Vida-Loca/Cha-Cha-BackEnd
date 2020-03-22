INSERT INTO Role VALUES (1, 'USER'), (2, 'ADMIN');
INSERT INTO User VALUES (1, 'test1@o2.pl', true, '2001-01-01', 'Test1', 'test1', 'url1', 'Test1', 'testowy1', 1);
INSERT INTO User VALUES (2, 'test2@o2.pl', true, '2001-01-02', 'Test2', 'test2', 'url2', 'Test2', 'testowy2', 1);
INSERT INTO User VALUES (3, 'test3@o2.pl', true, '2001-01-03', 'Test3', 'test3', 'url3', 'Test3', 'testowy3', 1);

INSERT INTO Address VALUES (1, 'TCity', 'TCountry', 'TNo', 'TCode', 'TStreet');

INSERT INTO Event VALUES (1, 'info', 'TestEvent', '2020-03-03', '10:00:00', 1);
INSERT INTO Event VALUES (2, 'info', 'EventToDelete', '2020-04-03', '10:00:00', 1);
INSERT INTO Event VALUES (3, 'info', 'EventNoEventUser', '2020-05-03', '10:00:00', 1);
INSERT INTO Event_user VALUES (1, true, 1, 1);
INSERT INTO Event_user VALUES (2, true, 2, 2);
INSERT INTO Event_user VALUES (3, false, 2, 1);

INSERT INTO Product_category VALUES (1, 'DRINK'), (2, 'FOOD');
INSERT INTO Product VALUES (1, 'TestCoke', '2.3', 1);

INSERT INTO User_card VALUES (1, 1 ,1);

INSERT INTO verification_token VALUES (1, '2200-01-01', 'TOKEN', 1);
INSERT INTO verification_token VALUES (2, '2001-01-01', 'EXTOKEN', 1);