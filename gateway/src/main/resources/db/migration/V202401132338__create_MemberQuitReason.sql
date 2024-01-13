CREATE TABLE `member_quit_reason`
(
    `member_id`  CHAR(26)  NOT NULL COMMENT '사용자아이디',
    `reason_id` VARCHAR(255) NOT NULL COMMENT '탈퇴사유',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`member_id`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci comment '탈퇴이유관리테이블';
