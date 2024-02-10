CREATE TABLE `family_top_percentage_history`
(
    `family_id` CHAR(26) NOT NULL,
    `date`      DATE     NOT NULL,
    `topPercentage`     INTEGER  NOT NULL,
    PRIMARY KEY (`family_id`, `date`),
    INDEX       `family_score_history_idx1` (`family_id`, `date`)
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '가족 상위 백분율 이력';