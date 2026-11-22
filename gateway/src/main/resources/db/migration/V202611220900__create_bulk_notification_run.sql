CREATE TABLE bulk_notification_run (
    notification_id VARCHAR(100) NOT NULL PRIMARY KEY,
    target_count INT NOT NULL,
    accepted_count INT NOT NULL DEFAULT 0,
    failed_count INT NOT NULL DEFAULT 0,
    unknown_count INT NOT NULL DEFAULT 0,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP NULL DEFAULT NULL
);
