package prm.projectbase.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompleteWorkoutRequest {
    @NotNull(message = "Total reps is required")
    @Min(value = 0, message = "Total reps must be greater than or equal to 0")
    Integer totalReps;

    @NotNull(message = "Total sets is required")
    @Min(value = 0, message = "Total sets must be greater than or equal to 0")
    Integer totalSets;

    @NotNull(message = "Duration is required")
    @Min(value = 0, message = "Duration must be greater than or equal to 0")
    Integer durationSeconds;

    @NotNull(message = "Average posture score is required")
    @DecimalMin(value = "0.0", message = "Average posture score must be at least 0")
    @DecimalMax(value = "100.0", message = "Average posture score must be at most 100")
    Double avgPostureScore;

    String aiFeedback;

    @DecimalMin(value = "0.0", message = "Calories burned must be greater than or equal to 0")
    Double caloriesBurned;
}
