ALTER TABLE family_top_percentage_history
DROP PRIMARY KEY;

ALTER TABLE family_top_percentage_history
DROP COLUMN date;

ALTER TABLE family_top_percentage_history
ADD COLUMN history_year INT NOT NULL DEFAULT 2024;

ALTER TABLE family_top_percentage_history
ADD COLUMN history_month INT NOT NULL DEFAULT 1;

ALTER TABLE family_top_percentage_history
ADD PRIMARY KEY (family_id, history_year, history_month);