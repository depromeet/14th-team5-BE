ALTER TABLE family_top_percentage_history
DROP COLUMN date;

ALTER TABLE family_top_percentage_history
ADD COLUMN history_year INT NOT NULL DEFAULT 2024;

ALTER TABLE family_top_percentage_history
ADD COLUMN history_month INT NOT NULL DEFAULT 1;


