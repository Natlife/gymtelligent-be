package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Entity
@Table(name = "user_streaks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStreak extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "current_streak")
    @Builder.Default
    Integer currentStreak = 0;

    @Column(name = "longest_streak")
    @Builder.Default
    Integer longestStreak = 0;

    @Column(name = "last_workout_date")
    LocalDate lastWorkoutDate;
}
