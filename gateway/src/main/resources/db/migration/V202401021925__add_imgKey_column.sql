ALTER TABLE `member` ADD COLUMN `profile_img_key` VARCHAR(255);
ALTER TABLE `member_post` CHANGE COLUMN `image_url` `post_img_url` TEXT NOT NULL;
ALTER TABLE `member_post` ADD COLUMN `post_img_key` VARCHAR(255) NOT NULL;
