package prm.projectbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsSummaryResponse {
    Integer totalWorkouts;
    Double totalCalories;
    Integer currentStreak;
    Integer longestStreak;
}
