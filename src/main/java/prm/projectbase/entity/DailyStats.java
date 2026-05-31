package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Entity
@Table(name = "daily_stats", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_stat_date", columnNames = {"user_id", "stat_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyStats extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "stat_date", nullable = false)
    LocalDate statDate;

    @Column(name = "total_calories")
    @Builder.Default
    Double totalCalories = 0.0;

    @Column(name = "total_duration_seconds")
    @Builder.Default
    Integer totalDurationSeconds = 0;

    @Column(name = "total_reps")
    @Builder.Default
    Integer totalReps = 0;

    @Column(name = "workout_count")
    @Builder.Default
    Integer workoutCount = 0;

    @Column(name = "avg_posture_score")
    @Builder.Default
    Double avgPostureScore = 0.0;
}
