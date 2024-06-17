ALTER TABLE `deep_link` ADD COLUMN (`member_id` CHAR(26) NOT NULL COMMENT 'ULID');
ALTER TABLE `deep_link` ADD COLUMN (`family_id` CHAR(26) NOT NULL COMMENT 'ULID');
ALTER TABLE `family_invite_link` ADD COLUMN (`member_id` CHAR(26) NOT NULL COMMENT 'ULID');
