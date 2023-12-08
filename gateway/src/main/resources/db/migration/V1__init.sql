CREATE TABLE `app_version`
(
    `app_key`     CHAR(36) PRIMARY KEY COMMENT 'UUID',
    `app_version` VARCHAR(36) NOT NULL,
    `in_service`  BOOL        NOT NULL,
    `in_review`   BOOL        NOT NULL,
    `created_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '앱버전관리테이블';

CREATE TABLE `member`
(
    `member_id`       CHAR(26)    NOT NULL COMMENT 'ULID',
    `family_id`       CHAR(26)             DEFAULT NULL,
    `day_of_birth`    DATE        NOT NULL,
    `name`            VARCHAR(36) NOT NULL,
    `profile_img_url` TEXT,
    `created_at`      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`      TIMESTAMP NULL,
    INDEX             `member_idx1` (`family_id`),
    PRIMARY KEY (`member_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '사용자테이블';

CREATE TABLE `social_member`
(
    `provider`   VARCHAR(16) NOT NULL COMMENT 'APPLE|KAKAO',
    `identifier` VARCHAR(64) NOT NULL,
    `member_id`  CHAR(26)    NOT NULL,
    `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY `social_member_fk1` (member_id) REFERENCES `member`(member_id),
    PRIMARY KEY (`provider`, `identifier`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '소셜로그인사용자테이블';

CREATE TABLE `member_device`
(
    `member_id`  CHAR(26)     NOT NULL,
    `fcm_token`  VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY `member_device_fk1` (member_id) REFERENCES `member`(member_id),
    INDEX        `member_device_idx1` (`fcm_token`),
    PRIMARY KEY (`member_id`, `fcm_token`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '사용자기기테이블';

CREATE TABLE `family`
(
    `family_id`  CHAR(26)  NOT NULL COMMENT 'ULID',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`family_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '가족테이블';

CREATE TABLE `member_post`
(
    `post_id`     CHAR(26)  NOT NULL COMMENT 'ULID',
    `member_id`   CHAR(26)  NOT NULL,
    `post_date`   DATE      NOT NULL,
    `image_url`   TEXT,
    `comment_cnt` INTEGER   NOT NULL DEFAULT 0,
    `emoji_cnt`   INTEGER   NOT NULL DEFAULT 0,
    `created_at`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX         `member_post_idx1` (`member_id`),
    PRIMARY KEY (`post_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '게시물테이블';

CREATE TABLE `member_post_emoji`
(
    `emoji_id`   CHAR(26)    NOT NULL COMMENT 'ULID',
    `post_id`    CHAR(26)    NOT NULL,
    `member_id`  CHAR(26)    NOT NULL,
    `emoji`      VARCHAR(16) NOT NULL,
    `created_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY `member_post_emoji_pk1` (post_id) REFERENCES `member_post`(`post_id`),
    INDEX        `member_post_emoji_idx1` (`post_id`),
    INDEX        `member_post_emoji_idx2` (`member_id`),
    PRIMARY KEY (`emoji_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '게시물이모지';

CREATE TABLE `member_post_comment`
(
    `comment_id` CHAR(26)     NOT NULL COMMENT 'ULID',
    `post_id`    CHAR(26)     NOT NULL,
    `member_id`  CHAR(26)     NOT NULL,
    `comment`    VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY `member_post_comment_pk1` (post_id) REFERENCES `member_post`(`post_id`),
    INDEX        `member_post_comment_idx1` (`post_id`),
    INDEX        `member_post_comment_idx2` (`member_id`),
    PRIMARY KEY (`comment_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '게시물댓글';
