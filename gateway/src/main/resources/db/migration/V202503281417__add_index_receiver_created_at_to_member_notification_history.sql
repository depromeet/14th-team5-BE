CREATE INDEX idx_receiver_created_at
    ON member_notification_history (receiver_member_id, created_at DESC);
