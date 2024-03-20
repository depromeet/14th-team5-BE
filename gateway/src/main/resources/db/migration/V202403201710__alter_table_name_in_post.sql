ALTER TABLE `member_post_reaction`
    DROP FOREIGN KEY `member_post_reaction_ibfk_1`,
    DROP INDEX `member_post_reaction_idx1`,
    DROP INDEX `member_post_reaction_idx2`;
ALTER TABLE `member_post_comment`
    DROP FOREIGN KEY `member_post_comment_ibfk_1`,
    DROP INDEX `member_post_comment_idx1`,
    DROP INDEX `member_post_comment_idx2`;
ALTER TABLE `member_post_real_emoji`
    DROP FOREIGN KEY `member_post_real_emoji_ibfk_1`,
    DROP FOREIGN KEY `member_post_real_emoji_ibfk_2`,
    DROP INDEX `member_post_real_emoji_idx1`,
    DROP INDEX `member_post_real_emoji_idx2`;

ALTER TABLE `member_real_emoji`
    DROP PRIMARY KEY,
    CHANGE COLUMN `real_emoji_id` `member_real_emoji_id` CHAR(26) NOT NULL,
    ADD PRIMARY KEY (`member_real_emoji_id`);
ALTER TABLE `member_post` RENAME `post`,
    DROP INDEX `member_post_idx1`,
    ADD INDEX `post_idx1` (`member_id`);
ALTER TABLE `member_post_reaction` RENAME `reaction`,
    ADD INDEX `reaction_idx1` (`post_id`),
    ADD INDEX `reaction_idx2` (`member_id`),
    ADD FOREIGN KEY `reaction_fk1` (`post_id`) REFERENCES `post` (`post_id`);
ALTER TABLE `member_post_comment` RENAME `comment`,
    ADD INDEX `comment_idx1` (`post_id`),
    ADD INDEX `comment_idx2` (`member_id`),
    ADD FOREIGN KEY `comment_fk1` (`post_id`) REFERENCES `post` (`post_id`),
    CHANGE COLUMN `comment` `content` VARCHAR(255) NOT NULL;
ALTER TABLE `member_post_real_emoji` RENAME `real_emoji`,
    DROP PRIMARY KEY,
    CHANGE COLUMN `real_emoji_id` `member_real_emoji_id` CHAR(26) NOT NULL,
    CHANGE COLUMN `post_real_emoji_id` `real_emoji_id` CHAR(26) NOT NULL,
    ADD INDEX `real_emoji_idx1` (`post_id`),
    ADD INDEX `real_emoji_idx2` (`member_id`),
    ADD PRIMARY KEY (`real_emoji_id`),
    ADD FOREIGN KEY `real_emoji_fk1` (`post_id`) REFERENCES `post` (`post_id`),
    ADD FOREIGN KEY `real_emoji_fk2` (`member_real_emoji_id`) REFERENCES `member_real_emoji` (`member_real_emoji_id`);
