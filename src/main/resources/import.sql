-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Jose1', 'V');
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Jose2', 'V');
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (3, TRUE, 'USER', 'c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Jose3', 'V');
INSERT INTO IWUser (id, enabled, roles, username, password, first_name, last_name)
VALUES (4, TRUE, 'USER', 'd',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Jose4', 'V');

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
-- INSERT INTO SUBASTA (fecha_inicio, fecha_fin, enabled, ruta_imagen, precio, descripcion, nombre, user_id) VALUES ('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.00, 'Descripcion', 'Reloj', 1), ('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.0, 'Descripcion', 'Reloj', 1), ('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.0, 'Descripcion', 'Reloj', 1), ('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.0, 'Descripcion', 'Reloj', 1), ('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.0, 'Descripcion', 'Reloj', 1),('2022-01-01 00:00:00', '2022-01-01 00:00:00', true, '/img', 10.0, 'Descripcion', 'Reloj', 1); 