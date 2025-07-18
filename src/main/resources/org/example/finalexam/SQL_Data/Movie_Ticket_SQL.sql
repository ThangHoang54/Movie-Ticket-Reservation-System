CREATE TABLE Theater (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) unique,
    address VARCHAR(255)
);

create table User_ (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    contact_info VARCHAR(255),
    booking_history VARCHAR(255)
);

CREATE TABLE Screen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    timing INT,
    movie_name VARCHAR(255) unique,
    price DOUBLE,
    seat_available INT,
    theater_id INT,
    FOREIGN KEY (theater_id) REFERENCES Theater(id) ON DELETE SET NULL
);

CREATE TABLE Booking (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reserved_seat INT,
    booking_date DATE, 
    screen_id INT,
    user_id INT,
    FOREIGN KEY (screen_id) REFERENCES Screen(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES User_(id) ON DELETE SET NULL
);

CREATE TABLE Theater_Screen (
    theater_id INT REFERENCES Theater(id) ON DELETE CASCADE,
    screen_id INT REFERENCES Screen(id) ON DELETE CASCADE,
    PRIMARY KEY (theater_id, screen_id),
    FOREIGN KEY (theater_id) REFERENCES Theater(id) ON DELETE CASCADE,
    FOREIGN KEY (screen_id) REFERENCES Screen(id) ON DELETE CASCADE
);

CREATE TABLE User_Booking (
    user_id INT REFERENCES User_(id) ON DELETE CASCADE,
    booking_id INT REFERENCES Booking(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, booking_id),
    FOREIGN KEY (user_id) REFERENCES User_(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE
);

INSERT INTO Theater (name, address) VALUES
('Galaxy Cinema', '123 ABC Street'),
('Lotte Cinema', '456 DEF Avenue'),
('CGV Cinema', '789 GHI Road'),
('BHD Star', '111 JKL Drive'),
('Cinestar', '222 MNO Boulevard'),
('MegaStar', '333 PQR Street'),
('Platinum Cinema', '444 STU Avenue'),
('Cinebox', '555 VWX Road'),
('Starlight Cinema', '666 YZA Drive'),
('Empire Cinema', '777 BCD Boulevard'),
('Dreamplex', '888 EFG Street'),
('Cinema Paradiso', '999 HIJ Avenue'),
('Royal Cinema', '101 KLM Road'),
('Infinity Cinema', '102 NOP Drive'),
('Premier Cinema', '103 QRS Boulevard'),
('Grand Theater', '104 TUV Street'),
('Broadway Cinemas', '105 WXY Avenue'),
('Regal Cinema', '106 ZAB Road'),
('Savoy Theater', '107 CDE Drive'),
('Epic Cinema', '108 FGH Boulevard'),
('Sunset Cinema', '109 IJK Street'),
('Lumiere', '110 LMN Avenue'),
('Odeon', '111 OPQ Road'),
('Cosmos Cinema', '112 RST Drive'),
('Neon Lights Cinema', '113 UVW Boulevard'),
('Mystic Theater', '114 XYZ Street'),
('Crescent Cinemas', '115 ABC Avenue'),
('Union Cinema', '116 DEF Road'),
('Arc Light Cinema', '117 GHI Drive'),
('Paragon Theater', '118 JKL Boulevard'),
('Heritage Cinemas', '119 MNO Street'),
('Vista Cinema', '120 PQR Avenue'),
('Landmark Theater', '121 STU Road'),
('Pinnacle Cinemas', '122 VWX Drive'),
('Keystone Cinema', '123 YZA Boulevard');

INSERT INTO User_ (name, contact_info, booking_history) VALUES
('Le Do Gia Phuc', 'ledogiaphuc2801@gmail.com', 'Booking_1'),
('Nguyen Xuan Nhi', 'xuannhivas@gmail.com', 'Booking_2'),
('Le Pham Quang Dai', '19042005dai@gmail.com', 'Booking_3'),
('Bui Bao Ngoc Phuc', 'phucbuibaonhoc@gmail.com', 'Booking_4'),
('Vinny Trinh', 'vinnytrinh@gmail.com', 'Booking_5'),
('Dang Hoang My', 'danghoangmy1827@gmail.com', 'Booking_6'),
('Nguyen Anh Khoi', 'khoibancho@gmail.com', 'Booking_7'),
('RibiSachi', 'ribisachi@gmail.com', 'Booking_8'),
('Tran Ha Minh Nhat', 'tommyloveu62@gmail.com', 'Booking_9'),
('Huynh Thu', 'thuhuynh@gmail.com', 'Booking_10'),
('Tom Luong', 'tomluong@gmail.com', 'Booking_11'),
('Nguyen Thuc Thuy Tien', 'Talents@senvangvn.com', 'Booking_12'),
('Truong The Vinh', 'ttvart@gmail.com', 'Booking_13'),
('Le Ngoc Minh Hang', 'minhhangentertainment@gmail.com', 'Booking_14'),
('Soobin Hoang Son', 'sibunny0312@gmail.com', 'Booking_15'),
('Thieu Bao Trang', 'Thieubaotrangbooking@example.com', 'Booking_16'),
('Hung Huynh', 'hunghuynh@nomadmgmt.vn', 'Booking_17'),
('Ninh Duong Lan Ngoc', 'duytuongle@gmail.com', 'Booking_18'),
('Son Tung MTP', 'booking@mtpentertainment.com', 'Booking_19'),
('Han So Hee', 'hansohee181193@gmail.com', 'Booking_20');

-- Inserting 100 unique movie entries into the Screen table
INSERT INTO Screen (timing, movie_name, price, seat_available, theater_id) VALUES
(135, 'Echoes of Eternity', 1.23, 120, 1),
(140, 'Journey to the Stars', 2.32, 80, 1),
(123, 'Midnight Mysteries', 2.34, 95, 2),
(160, 'Quantum Chronicles', 1.5, 110, 2),
(113, 'Lost in Time', 1.75, 1.42, 3),
(145, 'Whispers of the Past', 1.8, 85, 3),
(133, 'Parallel Worlds', 1.9, 100, 4),
(121, 'Enchanted Forest', 1.2, 90, 4),
(141, 'The Last Adventure', 1.2, 105, 5),
(150, 'Dreams of Tomorrow', 1.2, 115, 5),
(135, 'Silent Shadows', 1.2, 120, 6),
(147, 'Wandering Souls', 1.2, 80, 6),
(138, 'Secrets of the Galaxy', 1.2,  95, 7),
(158, 'Avalon Awakens', 1.2, 110, 7),
(116, 'Beyond the Horizon', 1.2, 75, 8),
(149, 'Phantom Realms', 1.2, 85, 8),
(131, 'Mystic Legends', 1.2, 100, 9),
(125, 'Eternal Night', 1.2,  90, 9),
(139, 'Guardians of Light', 1.2, 105, 10),
(151, 'Shadows of the Deep', 1.2, 115, 10),
(120, 'Beyond the Mirror', 1.25, 115, 1),
(130, 'Lost Treasures', 3.2, 110, 11),
(147, 'Into the Wild', 2.2,  105, 22),
(139, 'Secrets of Atlantis',1.2,  100, 23),
(142, 'Winds of Change', 1.2, 95, 21),
(148, 'Echoes of the Abyss', 1.2, 90, 32),
(150, 'Realm of the Giants', 1.2, 120, 12),
(132, 'Skybound', 1.2, 85, 13),
(136, 'The Wandering Isles', 1.2, 80, 25),
(140, 'Mysteries of the Mind', 1.2, 110, 26),
(123, 'Chronicles of the Future',1.2, 95, 27),
(158, 'Saga of the Stars', 1.2, 120, 18),
(117, 'The Hidden Temple', 1.2, 75, 24),
(139, 'Legacy of the Ancients',  1.2, 105, 28),
(134, 'Phantom of the Opera', 1.2, 110, 29),
(116, 'Shadows in the Mist', 1.2, 95, 29),
(150, 'Beyond the Stars',1.2,120, 24),
(139, 'Destiny Awaits', 1.2,110, 33),
(125, 'Echoes of the Past', 1.2,80, 35),
(132, 'Adventures in Wonderland', 1.2,95, 35),
(138, 'Guardians of the Galaxy', 1.2,115, 17),
(144, 'Labyrinth of Fate', 1.2,85, 20),
(129, 'Crystal Kingdom', 1.2,100, 20),
(146, 'Legends of the Hidden Temple', 1.2,120, 19),
(121, 'Mystic Island', 1.2,75, 18),
(147, 'Tales of the Dragon', 1.2,110, 13),
(129, 'Aurora', 1.2, 105, 14),
(137, 'Enigma', 1.2,100, 27),
(130, 'Echo', 1.2,95, 24),
(148, 'Elysium',1.2, 85, 28),
(140, 'Serendipity', 1.2,120, 29),
(134, 'Phenomenon', 1.2,110, 17),
(125, 'Vortex', 1.2,105, 8),
(144, 'Continuum', 1.2,90, 9),
(136, 'Utopia', 1.2,95, 10),
(121, 'Paradox', 1.2,120, 20),
(146, 'Odyssey',1.2,  80, 30),
(138, 'Labyrinth',1.2, 105, 30),
(140, 'Mirage',1.2, 110, 22),
(124, 'Eclipse', 1.2,95, 30),
(130, 'Crystal', 1.2,85, 3),
(147, 'Genesis',1.2, 100, 4),
(134, 'Epiphany',1.2,75, 4),
(142, 'Mosaic', 1.2,120, 5),
(136, 'Atlas', 1.2,105, 6),
(131, 'Nebula', 1.2,110, 11),
(125, 'Arcadia', 1.2,120, 18),
(139, 'Aurora Borealis', 1.2,105, 15),
(143, 'Pinnacle',1.2, 85, 16),
(145, 'Equinox',1.2, 90, 24),
(120, 'Anthem', 1.2,80, 34),
(138, 'Nirvana',1.2, 95, 22),
(151, 'Renaissance', 1.2,110, 21),
(141, 'Spectacle', 1.2,100, 32),
(137, 'Zenith', 1.2,115, 17),
(130, 'Reverie',1.2, 120, 15),
(124, 'Synergy', 1.2,110, 25),
(178, 'Revelation', 1.2,120, 11),
(145, 'Bo Tu Bao Thu',3.2, 90, 10),
(149, 'Infinity', 4.3,115, 10),
(147, 'Euphoria',1.2, 80, 23),
(141, 'Panorama',1.2, 95, 32),
(136, 'Whispers of Eternity', 1.2,120, 13),
(148, 'Journey Through Time', 1.2,110, 16),
(142, 'Twilight of Mysteries', 1.2,115, 33),
(141, 'Quantum Destinies',1.2, 105, 34),
(120, 'Lost Realm', 1.2,90, 35),
(134, 'Aetherial Tales', 1.2,85, 27),
(147, 'Horizon of Dreams', 1.2,95, 29),
(150, 'The Final Countdown', 1.2,100, 17),
(143, 'Timeless Legends',1.2,105, 15),
(136, 'Beyond the Veil', 1.2,75, 8),
(128, 'Echoes of the Ancients', 1.2,110, 19),
(141, 'The Forgotten Path', 1.2,120, 24),
(130, 'Elysian Dreams', 1.2,115, 8),
(144, 'Aurora Rising', 1.2,105, 9),
(139, 'Mystic Visions',1.2, 80, 10),
(138, 'Crescent Moon',1.2, 90, 1),
(147, 'Ephemeral Echoes', 1.2,120, 2),
(125, 'Shadow of the Moon', 1.2,105, 3),
(142, 'Chronicles of Light', 1.2,100, 4),
(136, 'Galactic Odyssey', 1.2,110, 5);

-- Insert 100 rows manually
INSERT INTO Booking (reserved_seat, booking_date, screen_id, user_id)
VALUES
    (1, '2024-01-05', 1, 1),
    (2, '2024-01-06', 2, 2),
    (3, '2024-01-07', 3, 3),
    (4, '2024-01-08', 4, 4),
    (5, '2024-01-09', 5, 5),
    (6, '2024-01-10', 6, 6),
    (7, '2024-01-11', 7, 7),
    (8, '2024-01-12', 8, 8),
    (9, '2024-01-13', 9, 9),
    (10, '2024-01-14', 10, 10),
    (11, '2024-01-15', 11, 11),
    (12, '2024-01-16', 12, 12),
    (13, '2024-01-17', 13, 13),
    (14, '2024-01-18', 14, 14),
    (15, '2024-01-19', 15, 15),
    (16, '2024-01-20', 16, 16),
    (17, '2024-01-21', 17, 17),
    (18, '2024-01-22', 18, 18),
    (19, '2024-01-23', 19, 19),
    (20, '2024-01-24', 20, 20),
    (21, '2024-01-25', 1, 1),
    (22, '2024-01-26', 2, 2),
    (23, '2024-01-27', 3, 3),
    (24, '2024-01-28', 4, 4),
    (25, '2024-01-29', 5, 5),
    (26, '2024-02-01', 6, 6),
    (27, '2024-02-02', 7, 7),
    (28, '2024-02-03', 8, 8),
    (29, '2024-02-04', 9, 9),
    (30, '2024-02-05', 10, 10),
    (31, '2024-02-06', 11, 11),
    (32, '2024-02-07', 12, 12),
    (33, '2024-02-08', 13, 13),
    (34, '2024-02-09', 14, 14),
    (35, '2024-02-10', 15, 15),
    (36, '2024-02-11', 16, 16),
    (37, '2024-02-12', 17, 17),
    (38, '2024-02-13', 18, 18),
    (39, '2024-02-14', 19, 19),
    (40, '2024-02-15', 20, 20),
    (41, '2024-02-16', 1, 1),
    (42, '2024-02-17', 2, 2),
    (43, '2024-02-18', 3, 3),
    (44, '2024-02-19', 4, 4),
    (45, '2024-02-20', 5, 5),
    (46, '2024-02-21', 6, 6),
    (47, '2024-02-22', 7, 7),
    (48, '2024-02-23', 8, 8),
    (49, '2024-02-24', 9, 9),
    (50, '2024-02-25', 10, 10),
    (51, '2025-01-05', 11, 11),
    (52, '2025-01-06', 12, 12),
    (53, '2025-01-07', 13, 13),
    (54, '2025-01-08', 14, 14),
    (55, '2025-01-09', 15, 15),
    (56, '2025-01-10', 16, 16),
    (57, '2025-01-11', 17, 17),
    (58, '2025-01-12', 18, 18),
    (59, '2025-01-13', 19, 19),
    (60, '2025-01-14', 20, 20),
    (61, '2025-01-15', 1, 1),
    (62, '2025-01-16', 2, 2),
    (63, '2025-01-17', 3, 3),
    (64, '2025-01-18', 4, 4),
    (65, '2025-01-19', 5, 5),
    (66, '2025-01-20', 6, 6),
    (67, '2025-01-21', 7, 7),
    (68, '2025-01-22', 8, 8),
    (69, '2025-01-23', 9, 9),
    (70, '2025-01-24', 10, 10),
    (71, '2025-01-25', 11, 11),
    (72, '2025-01-26', 12, 12),
    (73, '2025-01-27', 13, 13),
    (74, '2025-01-28', 14, 14),
    (75, '2025-01-29', 15, 15),
    (76, '2025-02-01', 16, 16),
    (77, '2025-02-02', 17, 17),
    (78, '2025-02-03', 18, 18),
    (79, '2025-02-04', 19, 19),
    (80, '2025-02-05', 20, 20),
    (81, '2025-02-06', 1, 1),
    (82, '2025-02-07', 2, 2),
    (83, '2025-02-08', 3, 3),
    (84, '2025-02-09', 4, 4),
    (85, '2025-02-10', 5, 5),
    (86, '2025-02-11', 6, 6),
    (87, '2025-02-12', 7, 7),
    (88, '2025-02-13', 8, 8),
    (89, '2025-02-14', 9, 9),
    (90, '2025-02-15', 10, 10),
    (91, '2025-02-16', 11, 11),
    (92, '2025-02-17', 12, 12),
    (93, '2025-02-18', 13, 13),
    (94, '2025-02-19', 14, 14),
    (95, '2025-02-20', 15, 15),
    (96, '2025-02-21', 16, 16),
    (97, '2025-02-22', 17, 17),
    (98, '2025-02-23', 18, 18),
    (99, '2025-02-24', 19, 19),
    (100, '2025-02-25', 20, 20);