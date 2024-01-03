CREATE TABLE `deep_link`
(
    `link_id` CHAR(8) NOT NULL,
    `type` VARCHAR(64) NOT NULL,
    `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '앱버전관리테이블';
