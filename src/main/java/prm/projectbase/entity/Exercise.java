package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exercise extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;

    @Enumerated(EnumType.STRING)
    ExerciseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    FitnessLevel difficultyLevel;

    @Column(name = "met_value", nullable = false)
    Double metValue;

    String description;

    @Column(name = "muscle_groups")
    String muscleGroups;

    @Column(name = "image_url", length = 500)
    String imageUrl;

    @Column(columnDefinition = "TEXT")
    String instructions;

    @Column(name = "default_sets")
    @Builder.Default
    Integer defaultSets = 3;

    @Column(name = "default_reps")
    @Builder.Default
    Integer defaultReps = 12;
}
