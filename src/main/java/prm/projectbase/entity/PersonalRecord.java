package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "personal_records", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_exercise", columnNames = {"user_id", "exercise_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    Exercise exercise;

    @Column(name = "max_reps")
    Integer maxReps;

    @Column(name = "max_duration_seconds")
    Integer maxDurationSeconds;

    @Column(name = "min_rest_time_seconds")
    Integer minRestTimeSeconds;

    @Column(name = "achieved_at")
    LocalDateTime achievedAt;
}
