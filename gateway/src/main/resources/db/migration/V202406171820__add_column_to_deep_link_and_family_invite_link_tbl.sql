ALTER TABLE `deep_link` ADD COLUMN (`member_id` CHAR(26) COMMENT 'ULID');
ALTER TABLE `deep_link` ADD COLUMN (`family_id` CHAR(26) COMMENT 'ULID');
ALTER TABLE `family_invite_link` ADD COLUMN (`member_id` CHAR(26) COMMENT 'ULID');
DELETE FROM `deep_link` WHERE `family_id` IS NULL OR `member_id` IS NULL;
DELETE FROM `family_invite_link` WHERE `member_id` IS NULL;
ALTER TABLE `deep_link` MODIFY COLUMN `member_id` CHAR(26) NOT NULL;
ALTER TABLE `deep_link` MODIFY COLUMN `family_id` CHAR(26) NOT NULL;
ALTER TABLE `family_invite_link` MODIFY COLUMN `member_id` CHAR(26) NOT NULL;
