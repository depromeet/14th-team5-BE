CREATE TABLE `voice_comment`
(
    `voice_comment_id` CHAR(26)     NOT NULL COMMENT 'ULID',
    `post_id`    CHAR(26)     NOT NULL,
    `member_id`  CHAR(26)     NOT NULL,
    `audio_url`  TEXT         NOT NULL,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY `voice_comment_pk1` (post_id) REFERENCES `post`(`post_id`),
    INDEX        `voice_comment_idx1` (`post_id`),
    INDEX        `voice_comment_idx2` (`member_id`),
    PRIMARY KEY (`voice_comment_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '게시물음성댓글';