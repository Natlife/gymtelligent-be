package prm.projectbase.controller;

import prm.projectbase.entity.Exercise;
import prm.projectbase.entity.ExerciseCategory;
import prm.projectbase.entity.FitnessLevel;
import prm.projectbase.dto.response.BaseResponse;
import prm.projectbase.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {

    ExerciseRepository exerciseRepository;

    @GetMapping
    public BaseResponse<List<Exercise>> getExercises(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level) {
        
        List<Exercise> exercises;
        
        if (category != null && level != null) {
            exercises = exerciseRepository.findByCategoryAndDifficultyLevel(
                    ExerciseCategory.valueOf(category.toUpperCase()),
                    FitnessLevel.valueOf(level.toUpperCase())
            );
        } else if (category != null) {
            exercises = exerciseRepository.findByCategory(
                    ExerciseCategory.valueOf(category.toUpperCase())
            );
        } else if (level != null) {
            exercises = exerciseRepository.findByDifficultyLevel(
                    FitnessLevel.valueOf(level.toUpperCase())
            );
        } else {
            exercises = exerciseRepository.findAll();
        }
        
        return BaseResponse.success(exercises, "Fetched exercises successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<Exercise> getExerciseById(@PathVariable Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
        return BaseResponse.success(exercise, "Fetched exercise detail successfully");
    }
}
