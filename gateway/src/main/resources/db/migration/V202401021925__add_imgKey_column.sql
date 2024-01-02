ALTER TABLE `member` ADD COLUMN `profile_img_key` VARCHAR(255);
ALTER TABLE `member_post` MODIFY COLUMN `image_url` `post_img_url`, ADD COLUMN `post_img_key` VARCHAR(255) NOT NULL;
