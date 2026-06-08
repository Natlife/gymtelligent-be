package prm.projectbase.seeder;

import prm.projectbase.entity.Exercise;
import prm.projectbase.entity.ExerciseCategory;
import prm.projectbase.entity.FitnessLevel;
import prm.projectbase.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ExerciseDataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;

    public ExerciseDataSeeder(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Exercise> targetExercises = List.of(
            Exercise.builder()
                .name("Squats")
                .category(ExerciseCategory.STRENGTH)
                .difficultyLevel(FitnessLevel.BEGINNER)
                .metValue(5.0)
                .description("An essential lower body movement focusing on quadriceps, hamstrings, and glutes. Great for functional strength, flexibility, and power.")
                .muscleGroups("quadriceps,hamstrings,glutes,core")
                .instructions("[\"Stand with feet shoulder-width apart, toes slightly outward\",\"Lower hips back and down as if sitting in a chair\",\"Keep your chest high and knees behind your toes\",\"Drive through your heels to return to standing position\",\"Squeeze glutes at the top and repeat\"]")
                .defaultSets(4)
                .defaultReps(12)
                .build(),
            Exercise.builder()
                .name("Barbell Biceps Curl")
                .category(ExerciseCategory.STRENGTH)
                .difficultyLevel(FitnessLevel.INTERMEDIATE)
                .metValue(4.5)
                .description("An isolated upper body exercise focusing specifically on the biceps brachii, helping build arm strength, muscle mass, and grip strength.")
                .muscleGroups("biceps,forearms,shoulders")
                .instructions("[\"Stand up straight with feet shoulder-width apart holding a barbell\",\"Keep elbows close to your torso, curl weights while contracting biceps\",\"Raise the bar until shoulder level, squeeze biceps at the top\",\"Slowly lower the barbell back to the starting position\",\"Repeat for desired reps\"]")
                .defaultSets(3)
                .defaultReps(12)
                .build(),
            Exercise.builder()
                .name("Shoulder Press")
                .category(ExerciseCategory.STRENGTH)
                .difficultyLevel(FitnessLevel.INTERMEDIATE)
                .metValue(5.0)
                .description("A powerhouse overhead pressing movement targeting the deltoids, triceps, and upper chest, essential for building shoulder strength and stability.")
                .muscleGroups("shoulders,triceps,upper chest,core")
                .instructions("[\"Hold the barbell or dumbbells at shoulder height with palms facing forward\",\"Keep your core tight and press the weight straight overhead\",\"Extend arms fully without locking elbows at the top\",\"Slowly lower the weight back to shoulder level under control\",\"Repeat for desired reps\"]")
                .defaultSets(3)
                .defaultReps(12)
                .build()
        );

        for (Exercise target : targetExercises) {
            java.util.Optional<Exercise> existingOpt = exerciseRepository.findByName(target.getName());
            if (existingOpt.isPresent()) {
                Exercise existing = existingOpt.get();
                existing.setCategory(target.getCategory());
                existing.setDifficultyLevel(target.getDifficultyLevel());
                existing.setMetValue(target.getMetValue());
                existing.setDescription(target.getDescription());
                existing.setMuscleGroups(target.getMuscleGroups());
                existing.setInstructions(target.getInstructions());
                existing.setDefaultSets(target.getDefaultSets());
                existing.setDefaultReps(target.getDefaultReps());
                exerciseRepository.save(existing);
            } else {
                exerciseRepository.save(target);
            }
        }

        // Clean up the unsupported "Deadlifts" exercise if present
        java.util.Optional<Exercise> deadliftOpt = exerciseRepository.findByName("Deadlifts");
        if (deadliftOpt.isPresent()) {
            try {
                exerciseRepository.delete(deadliftOpt.get());
                System.out.println("   REMOVED UNSUPPORTED EXERCISE: DEADLIFTS   ");
            } catch (Exception e) {
                System.out.println("   COULD NOT DELETE DEADLIFTS DUE TO FOREIGN KEY, KEEPING IT DEACTIVATED   ");
            }
        }

        // Clean up the unsupported "Push Ups" exercise if present
        java.util.Optional<Exercise> pushupOpt = exerciseRepository.findByName("Push Ups");
        if (pushupOpt.isPresent()) {
            try {
                exerciseRepository.delete(pushupOpt.get());
                System.out.println("   REMOVED UNSUPPORTED EXERCISE: PUSH UPS   ");
            } catch (Exception e) {
                System.out.println("   COULD NOT DELETE PUSH UPS DUE TO FOREIGN KEY, KEEPING IT DEACTIVATED   ");
            }
        }

        System.out.println("   EXERCISES SEEDED & UPDATED SUCCESSFULLY FOR GYMTELLIGENT APP   ");
    }
}
