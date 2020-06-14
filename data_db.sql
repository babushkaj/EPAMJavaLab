INSERT INTO users(name, surname, login, password)
VALUES
	('One', 'First', 'One', '$2a$10$pRxUtD2Tns8hMB8wW41pyu8ZXW9OAV2rELzTQf14hC98Q8FWJ.lV2'),
	('Two', 'Second', 'Two', '$2a$10$TXN6.npWghVh7X2Ionn1S.l3/oiI6wzLMOZOw/v7IDLBlOgeLndre'),
	('Three', 'Third', 'Three', '$2a$10$1clMPZnbSGsLCWMLckTbduQj4wcFFr3vsHwp/a1dhAiaukoezpNju');
   
INSERT INTO news(title, short_text,	full_text, creation_date, modification_date)
VALUES
	('The first news', 'This is the short text of the first news', 'This is the very long full text of the first news', '2018-01-01', '2018-02-02'),
	('The second news', 'This is the short text of the second news', 'This is the very long full text of the second news', '2018-02-02', '2018-03-03'),
	('The third news', 'This is the short text of the third news', 'This is the very long full text of the third news', '2018-03-03', '2018-04-04'),
	('The fourth news', 'This is the short text of the fourth news', 'This is the very long full text of the fourth news', '2018-04-04', '2018-05-05'),
	('The fifth news', 'This is the short text of the fifth news', 'This is the very long full text of the fifth news', '2018-05-05', '2018-06-06'),
	('The sixth news', 'This is the short text of the sixth news', 'This is the very long full text of the sixth news', '2018-06-06', '2018-07-07'),
	('The seventh news', 'This is the short text of the seventh news', 'This is the very long full text of the seventh news', '2018-07-07', '2018-08-08'),
	('The eighth news', 'This is the short text of the eighth news', 'This is the very long full text of the eighth news', '2018-08-08', '2018-09-09'),
	('The ninth news', 'This is the short text of the ninth news', 'This is the very long full text of the ninth news', '2018-09-09', '2018-10-10'),
	('The tenth news', 'This is the short text of the tenth news', 'This is the very long full text of the tenth news', '2018-10-10', '2018-11-11'),
	('The eleventh news', 'This is the short text of the eleventh news', 'This is the very long full text of the eleventh news', '2018-01-01', '2018-02-02'),
	('The twelfth news', 'This is the short text of the twelfth news', 'This is the very long full text of the twelfth news', '2018-02-02', '2018-03-03'),
	('The thirteenth news', 'This is the short text of the thirteenth news', 'This is the very long full text of the thirteenth news', '2018-03-03', '2018-04-04'),
	('The fourteenth news', 'This is the short text of the fourteenth news', 'This is the very long full text of the fourteenth news', '2018-04-04', '2018-05-05'),
	('The fifteenth news', 'This is the short text of the fifteenth news', 'This is the very long full text of the fifteenth news', '2018-05-05', '2018-06-06'),
	('The sixteenth news', 'This is the short text of the sixteenth news', 'This is the very long full text of the sixteenth news', '2018-06-06', '2018-07-07'),
	('The seventeenth news', 'This is the short text of the seventeenth news', 'This is the very long full text of the seventeenth news', '2018-07-07', '2018-08-08'),
	('The eighteenth news', 'This is the short text of the eighteenth news', 'This is the very long full text of the eighteenth news', '2018-08-08', '2018-09-09'),
	('The nineteenth news', 'This is the short text of the nineteenth news', 'This is the very long full text of the nineteenth news', '2018-09-09', '2018-10-10'),
	('The twentieth news', 'This is the short text of the twentieth news', 'This is the very long full text of the twentieth news', '2018-10-10', '2018-11-11');

	
INSERT INTO author(name, surname)
VALUES
	('Authorone', 'Firstauthor'), ('Authortwo', 'Secondauthor'), ('Authorthree', 'Thirdauthor'), ('Authorfour', 'Fourthauthor'),
	('Authorfive', 'Fifthauthor'), ('Authorsix', 'Sixthauthor'), ('Authorseven', 'Seventhauthor'), ('Authoreight', 'Eighthauthor'),
	('Authornine', 'Ninthauthor'), ('Authorten', 'Tenthauthor'), ('Authoreleven', 'Eleventhauthor'), ('Authortwelve', 'Twelfthauthor'),
	('Authorthirteen', 'Thirteenthauthor'), ('Authorfourteen', 'Fourteenthauthor'), ('Authorfifteen', 'Fifteenthauthor'), ('Authorsixteen', 'Sixteenthauthor'),
	('Authorseventeen', 'Seventeethauthor'), ('Authoreightteen', 'Eighteenthauthor'), ('Authornineteen', 'Nintheenthauthor'), ('Authortwenteen', 'Twentiethauthor');
	
INSERT INTO tag(name)
VALUES
	('TagOne'), ('TagTwo'), ('TagThree'), ('TagFour'), ('TagFive'),
	('TagSix'), ('TagSeven'), ('TagEight'), ('TagNine'), ('TagTen'),
	('TagEleven'), ('TagTwelve'), ('TagThirteen'), ('TagFourteen'), ('TagFifteen'),
	('TagSixteen'), ('TagSeventeen'), ('TagEighteen'), ('TagNineteen'), ('TagTwenty');
	
INSERT INTO news_tag(news_id, tag_id)
VALUES
	(1, 1), (1, 2), (1, 3),
	(2, 2),	(2, 3),	(2, 4),
	(3, 3),	(3, 4),	(3, 5),
	(4, 4),	(4, 5),	(4, 6),
	(5, 5),	(5, 6),	(5, 7),
	(6, 6),	(6, 7),	(6, 8),
	(7, 7),	(7, 8),	(7, 9),
	(8, 8),	(8, 9),	(8, 10),
	(9, 9),	(9, 10), (9, 11),
	(10, 10), (10, 11),	(10, 12),
	(11, 11), (11, 12),	(11, 13),
	(12, 12), (12, 13),	(12, 14),
	(13, 13), (13, 14), (13, 15),
	(14, 14), (14, 15), (14, 16),
	(15, 15), (15, 16), (15, 17),
	(16, 16), (16, 17), (16, 18),
	(17, 17), (17, 18), (17, 19),
	(18, 18), (18, 19), (18, 20),
	(19, 19), (19, 20), (19, 1),
	(20, 20), (20, 1), (20, 2);
	
INSERT INTO news_author(news_id, author_id)
VALUES
	(1, 1), (2, 2), (3, 3),	(4, 4),	(5, 5),	
	(6, 6),	(7, 7),	(8, 8),	(9, 9),	(10, 10), 
	(11, 11), (12, 12),	(13, 13), (14, 14),	(15, 15), 
	(16, 16), (17, 17), (18, 18), (19, 19), (20, 20);
	
INSERT INTO roles(id, role_name)
VALUES
	(1, 'USER'), (2, 'ADMIN');
	
INSERT INTO user_role(user_id, role_id)
VALUES
	(1, 1), (1, 2), (2, 2),	(3, 2);

	
