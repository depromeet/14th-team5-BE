CREATE TABLE `member_real_emoji`
(
    `real_emoji_id`     CHAR(26)        NOT NULL    COMMENT 'ULID',
    `member_id`         CHAR(26)        NOT NULL    COMMENT 'ULID',
    `type`              VARCHAR(16)     NOT NULL,
    `real_emoji_image_url`   TEXT            NOT NULL,
    `real_emoji_image_key`   VARCHAR(255)    NOT NULL,
    `created_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`real_emoji_id`),
    INDEX       `member_real_emoji_idx1` (`member_id`)
) DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT '리얼이모지';

CREATE TABLE `member_post_real_emoji`
(
    `post_real_emoji_id`    CHAR(26)       NOT NULL    COMMENT 'ULID',
    `real_emoji_id`         CHAR(26)        NOT NULL    COMMENT 'ULID',
    `post_id`               CHAR(26)        NOT NULL    COMMENT 'ULID',
    `member_id`             CHAR(26)        NOT NULL    COMMENT 'ULID',
    `created_at`            TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`post_real_emoji_id`),
    FOREIGN KEY  `member_post_real_emoji_fk1` (`post_id`) REFERENCES `member_post` (`post_id`),
    FOREIGN KEY  `member_post_real_emoji_fk2` (`real_emoji_id`) REFERENCES `member_real_emoji` (`real_emoji_id`),
    INDEX        `member_post_real_emoji_idx1` (`post_id`),
    INDEX        `member_post_real_emoji_idx2` (`member_id`)
) DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci COMMENT '게시물리얼이모지';

ALTER TABLE `member_post` ADD COLUMN `real_emoji_cnt` INTEGER  NOT NULL  DEFAULT 0;
