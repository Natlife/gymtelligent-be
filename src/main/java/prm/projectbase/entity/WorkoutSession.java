package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_sessions", indexes = {
    @Index(name = "idx_user_session_date", columnList = "user_id, session_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkoutSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    Exercise exercise;

    @Column(name = "session_date", nullable = false)
    LocalDate sessionDate;

    @Column(name = "started_at")
    LocalDateTime startedAt;

    @Column(name = "ended_at")
    LocalDateTime endedAt;

    @Column(name = "duration_seconds")
    Integer durationSeconds;

    @Column(name = "total_reps")
    Integer totalReps;

    @Column(name = "total_sets")
    Integer totalSets;

    @Column(name = "calories_burned")
    Double caloriesBurned;

    @Column(name = "avg_posture_score")
    Double avgPostureScore;

    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    String aiFeedback;
}
