package prm.projectbase.repository;

import prm.projectbase.entity.Exercise;
import prm.projectbase.entity.ExerciseCategory;
import prm.projectbase.entity.FitnessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Optional<Exercise> findByName(String name);
    List<Exercise> findByCategory(ExerciseCategory category);
    List<Exercise> findByDifficultyLevel(FitnessLevel difficultyLevel);
    List<Exercise> findByCategoryAndDifficultyLevel(ExerciseCategory category, FitnessLevel difficultyLevel);
}
