ALTER TABLE post ADD COLUMN ai_post_type VARCHAR(50) NULL;
UPDATE post SET ai_post_type = 'CHUSEOK_2025' WHERE type = 'ai_image' AND created_at < '2025-12-14 00:00:00';
