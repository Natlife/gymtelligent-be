package prm.projectbase.dto.response;

import lombok.Builder;
import lombok.Getter;
import prm.projectbase.entity.Exercise;

@Getter
@Builder
public class ExerciseResponse {
    private Integer id;
    private String name;
    private String category;
    private String difficultyLevel;
    private Double metValue;
    private String description;
    private String muscleGroups;
    private String imageUrl;
    private String instructions;
    private Integer defaultSets;
    private Integer defaultReps;

    public static ExerciseResponse fromEntity(Exercise exercise) {
        if (exercise == null) {
            return null;
        }

        return ExerciseResponse.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .category(exercise.getCategory() == null ? null : exercise.getCategory().name())
                .difficultyLevel(exercise.getDifficultyLevel() == null ? null : exercise.getDifficultyLevel().name())
                .metValue(exercise.getMetValue())
                .description(exercise.getDescription())
                .muscleGroups(exercise.getMuscleGroups())
                .imageUrl(exercise.getImageUrl())
                .instructions(exercise.getInstructions())
                .defaultSets(exercise.getDefaultSets())
                .defaultReps(exercise.getDefaultReps())
                .build();
    }
}
