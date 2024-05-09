CREATE TABLE IF NOT EXISTS `mission`
(
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `content`    VARCHAR(255) NOT NULL,
    `mission_id` CHAR(26)     NOT NULL COMMENT 'UUID',
    PRIMARY KEY (`mission_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '미션테이블';

CREATE TABLE IF NOT EXISTS `daily_mission_history`
(
    `date`       DATE         NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mission_id` CHAR(26)     NOT NULL,
    FOREIGN KEY `daily_mission_history_fk1` (`mission_id`) REFERENCES `mission` (`mission_id`),
    PRIMARY KEY (`date`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '일일미션내역테이블';