ALTER TABLE `member_post` ADD COLUMN `content` VARCHAR(8) NOT NULL;
ALTER TABLE `member_post` MODIFY COLUMN `image_url` TEXT NOT NULL;
ALTER TABLE `member_post_reaction` CHANGE COLUMN `ascii` `emoji` VARCHAR(16) NOT NULL;
