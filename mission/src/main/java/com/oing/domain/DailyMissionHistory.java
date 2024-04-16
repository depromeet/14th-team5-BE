package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "daily_mission_history")
public class DailyMissionHistory extends BaseEntity {
    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", referencedColumnName = "mission_id", columnDefinition = "CHAR(26)", nullable = false)
    private Mission mission;
}
