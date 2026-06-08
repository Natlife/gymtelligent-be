package prm.projectbase.dto.response;

import lombok.Builder;
import lombok.Getter;
import prm.projectbase.entity.WorkoutSession;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class WorkoutSessionResponse {
    private Integer id;
    private ExerciseResponse exercise;
    private LocalDate sessionDate;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationSeconds;
    private Integer totalReps;
    private Integer totalSets;
    private Double caloriesBurned;
    private Double avgPostureScore;
    private String aiFeedback;

    public static WorkoutSessionResponse fromEntity(WorkoutSession session) {
        if (session == null) {
            return null;
        }

        return WorkoutSessionResponse.builder()
                .id(session.getId())
                .exercise(ExerciseResponse.fromEntity(session.getExercise()))
                .sessionDate(session.getSessionDate())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .durationSeconds(session.getDurationSeconds())
                .totalReps(session.getTotalReps())
                .totalSets(session.getTotalSets())
                .caloriesBurned(session.getCaloriesBurned())
                .avgPostureScore(session.getAvgPostureScore())
                .aiFeedback(session.getAiFeedback())
                .build();
    }
}
