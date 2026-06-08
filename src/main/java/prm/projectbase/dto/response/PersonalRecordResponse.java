package prm.projectbase.dto.response;

import lombok.Builder;
import lombok.Getter;
import prm.projectbase.entity.PersonalRecord;

import java.time.LocalDateTime;

@Getter
@Builder
public class PersonalRecordResponse {
    private Integer id;
    private ExerciseResponse exercise;
    private Integer maxReps;
    private Integer maxDurationSeconds;
    private Integer minRestTimeSeconds;
    private LocalDateTime achievedAt;

    public static PersonalRecordResponse fromEntity(PersonalRecord record) {
        if (record == null) {
            return null;
        }

        return PersonalRecordResponse.builder()
                .id(record.getId())
                .exercise(ExerciseResponse.fromEntity(record.getExercise()))
                .maxReps(record.getMaxReps())
                .maxDurationSeconds(record.getMaxDurationSeconds())
                .minRestTimeSeconds(record.getMinRestTimeSeconds())
                .achievedAt(record.getAchievedAt())
                .build();
    }
}
