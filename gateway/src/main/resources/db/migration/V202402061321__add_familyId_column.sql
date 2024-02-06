ALTER TABLE `member_post` ADD COLUMN (`family_id` CHAR(26) NOT NULL DEFAULT '0' COMMENT 'ULID');
UPDATE member_post JOIN member ON member_post.member_id = member.member_id
SET member_post.family_id = member.family_id;
