package com.oing.domain;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2/2/24
 * Time: 4:49 AM
 */
public record BulkNotificationCompletedEvent(
        String reason,
        int totalTargets,
        int totalMembers,
        long elapsedMillis
) {
}
