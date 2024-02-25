ALTER TABLE family_top_percentage_history
DROP COLUMN date;

ALTER TABLE family_top_percentage_history
ADD COLUMN year INT NOT NULL DEFAULT 2024;

ALTER TABLE family_top_percentage_history
ADD COLUMN month INT NOT NULL DEFAULT 1;


