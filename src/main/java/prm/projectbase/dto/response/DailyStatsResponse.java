package prm.projectbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyStatsResponse {
    LocalDate date;
    Double totalCalories;
    Integer totalDurationSeconds;
    Integer totalReps;
    Integer workoutCount;
    Double avgPostureScore;
}
