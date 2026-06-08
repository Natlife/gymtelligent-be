package prm.projectbase.controller;

import prm.projectbase.dto.request.CompleteWorkoutRequest;
import prm.projectbase.dto.request.StartWorkoutRequest;
import prm.projectbase.dto.response.BaseResponse;
import prm.projectbase.dto.response.PersonalRecordResponse;
import prm.projectbase.dto.response.WorkoutSessionResponse;
import prm.projectbase.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutController {

    WorkoutService workoutService;

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/start")
    public BaseResponse<WorkoutSessionResponse> startWorkout(@RequestBody @Valid StartWorkoutRequest request) {
        WorkoutSessionResponse session = workoutService.startSession(getCurrentUserId(), request.getExerciseId());
        return BaseResponse.success(session, "Workout session started successfully");
    }

    @PutMapping("/{id}/complete")
    public BaseResponse<WorkoutSessionResponse> completeWorkout(
            @PathVariable Integer id,
            @RequestBody @Valid CompleteWorkoutRequest request) {
        WorkoutSessionResponse session = workoutService.completeSession(
                id,
                getCurrentUserId(),
                request.getTotalReps(),
                request.getTotalSets(),
                request.getDurationSeconds(),
                request.getAvgPostureScore(),
                request.getAiFeedback(),
                request.getCaloriesBurned()
        );
        return BaseResponse.success(session, "Workout session completed successfully");
    }

    @GetMapping("/history")
    public BaseResponse<Page<WorkoutSessionResponse>> getWorkoutHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<WorkoutSessionResponse> history = workoutService.getHistory(getCurrentUserId(), page, size);
        return BaseResponse.success(history, "Fetched workout history successfully");
    }

    @GetMapping("/records")
    public BaseResponse<List<PersonalRecordResponse>> getPersonalRecords() {
        List<PersonalRecordResponse> records = workoutService.getPersonalRecords(getCurrentUserId());
        return BaseResponse.success(records, "Fetched personal records successfully");
    }
}
