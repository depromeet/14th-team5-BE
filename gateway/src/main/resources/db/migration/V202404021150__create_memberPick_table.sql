CREATE TABLE IF NOT EXISTS `member_pick` (
    `pick_id` CHAR(26) NOT NULL COMMENT 'ULID',
    `family_id` CHAR(26) NOT NULL COMMENT 'ULID',
    `from_member_id` CHAR(26) NOT NULL COMMENT 'ULID',
    `date` DATE NOT NULL,
    `to_member_id` CHAR(26) NOT NULL COMMENT 'ULID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `member_pick_idx1` (`family_id`, `from_member_id`, `date`, `to_member_id`),
    INDEX `member_pick_idx2` (`family_id`, `date`, `to_member_id`),
    PRIMARY KEY (`pick_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '사용자콕찌르기';
