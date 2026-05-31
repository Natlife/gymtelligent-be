package prm.projectbase.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompleteWorkoutRequest {
    Integer totalReps;
    Integer totalSets;
    Integer durationSeconds;
    Double avgPostureScore;
    String aiFeedback;
    Double caloriesBurned;
}
