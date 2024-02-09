ALTER TABLE `member_real_emoji` ADD COLUMN (`family_id` CHAR(26) NOT NULL DEFAULT '0' COMMENT 'ULID');
UPDATE member_real_emoji JOIN member ON member_real_emoji.member_id = member.member_id
SET member_real_emoji.family_id = member.family_id;
