CREATE TABLE `family_invite_link`
(
    `link_id`  CHAR(10)  NOT NULL COMMENT 'random string',
    `family_id` CHAR(26)  NOT NULL COMMENT '가족아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`family_id`),
    FOREIGN KEY `family_invite_link_fk1` (`family_id`) REFERENCES `family` (`family_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '가족초대링크관리테이블';
