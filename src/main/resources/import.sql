INSERT INTO T_AUTHORITY VALUES ('ROLE_ADMIN');
INSERT INTO T_AUTHORITY VALUES ('ROLE_USER');


INSERT INTO T_USER VALUES (1, 'System', '2016-01-01', 'System', '2016-01-01', true, 'admin', 'system@system.com', 'admin', 'fr', 'admin', 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC');
INSERT INTO T_USER VALUES (2, 'System', '2016-01-01', 'System', '2016-01-01', true, 'user', 'user@user.com', 'user', 'fr', 'user', 'user', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC');
INSERT INTO T_USER VALUES (3, 'System', '2016-01-01', 'System', '2016-01-01', true, 'anonymousUser', 'none@none.com', 'anonymousUser', 'fr', 'anonymousUser', 'anonymousUser', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC');


INSERT INTO T_USER_AUTHORITY VALUES (1, 'ROLE_ADMIN');
INSERT INTO T_USER_AUTHORITY VALUES (1, 'ROLE_USER');
INSERT INTO T_USER_AUTHORITY VALUES (2, 'ROLE_USER');
