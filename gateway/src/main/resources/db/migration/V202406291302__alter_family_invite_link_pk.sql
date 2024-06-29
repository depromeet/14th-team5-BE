ALTER TABLE `family_invite_link` DROP FOREIGN KEY `family_invite_link_fk1`;
ALTER TABLE `family_invite_link` DROP PRIMARY KEY;
ALTER TABLE `family_invite_link` ADD PRIMARY KEY (`link_id`);
