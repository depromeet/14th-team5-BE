ALTER TABLE `member` ADD COLUMN `family_join_at` TIMESTAMP COMMENT '가족가입일시';
UPDATE member SET family_join_at = created_at WHERE family_id IS NOT NULL;
