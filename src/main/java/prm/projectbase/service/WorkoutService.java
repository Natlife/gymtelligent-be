package prm.projectbase.service;

import prm.projectbase.entity.*;
import prm.projectbase.repository.*;
import prm.projectbase.exception.AppException;
import prm.projectbase.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutService {

    WorkoutSessionRepository workoutSessionRepository;
    UserRepository userRepository;
    ExerciseRepository exerciseRepository;
    UserProfileRepository userProfileRepository;
    DailyStatsRepository dailyStatsRepository;
    UserStreakRepository userStreakRepository;
    PersonalRecordRepository personalRecordRepository;
    CalorieCalculatorService calorieCalculatorService;

    @Transactional
    public WorkoutSession startSession(Integer userId, Integer exerciseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)); // Exercise not found

        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                .exercise(exercise)
                .sessionDate(LocalDate.now())
                .startedAt(LocalDateTime.now())
                .totalReps(0)
                .totalSets(0)
                .durationSeconds(0)
                .caloriesBurned(0.0)
                .avgPostureScore(0.0)
                .build();
        session.setStatus(0); // 0 = Ongoing

        return workoutSessionRepository.save(session);
    }

    @Transactional
    public WorkoutSession completeSession(Integer sessionId, Integer totalReps, Integer totalSets,
                                          Integer durationSeconds, Double avgPostureScore, String aiFeedback, Double caloriesBurned) {
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)); // Session not found

        if (session.getStatus() == 1) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Already completed
        }

        // Get user's weight for MET calculation
        double weightKg = 65.0; // Default fallback
        Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(session.getUser().getId());
        if (profileOpt.isPresent() && profileOpt.get().getWeightKg() != null) {
            weightKg = profileOpt.get().getWeightKg();
        }

        // Calculate calories burned (use client-side computed calories if available)
        double finalCalories = (caloriesBurned != null && caloriesBurned > 0) ? caloriesBurned : calorieCalculatorService.calculateCalories(
                session.getExercise().getMetValue(),
                weightKg,
                durationSeconds
        );

        // Update session info
        session.setEndedAt(LocalDateTime.now());
        session.setDurationSeconds(durationSeconds);
        session.setTotalReps(totalReps);
        session.setTotalSets(totalSets);
        session.setCaloriesBurned(finalCalories);
        session.setAvgPostureScore(avgPostureScore);
        session.setAiFeedback(aiFeedback);
        session.setStatus(1); // 1 = Completed

        WorkoutSession savedSession = workoutSessionRepository.save(session);

        // Pre-computed Stats Update
        updateDailyStats(session.getUser(), session.getSessionDate(), caloriesBurned, durationSeconds, totalReps, avgPostureScore);

        // Streak calculation
        updateStreakAfterWorkout(session.getUser(), session.getSessionDate());

        // Personal record update
        updatePersonalRecord(session.getUser(), session.getExercise(), totalReps, durationSeconds);

        return savedSession;
    }

    @Transactional(readOnly = true)
    public Page<WorkoutSession> getHistory(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        return workoutSessionRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void updateDailyStats(User user, LocalDate date, double calories, int durationSec, int reps, double postureScore) {
        DailyStats stats = dailyStatsRepository.findByUserIdAndStatDate(user.getId(), date)
                .orElseGet(() -> DailyStats.builder()
                        .user(user)
                        .statDate(date)
                        .totalCalories(0.0)
                        .totalDurationSeconds(0)
                        .totalReps(0)
                        .workoutCount(0)
                        .avgPostureScore(0.0)
                        .build());

        stats.setTotalCalories(stats.getTotalCalories() + calories);
        stats.setTotalDurationSeconds(stats.getTotalDurationSeconds() + durationSec);
        stats.setTotalReps(stats.getTotalReps() + reps);
        stats.setWorkoutCount(stats.getWorkoutCount() + 1);

        double totalScore = stats.getAvgPostureScore() * (stats.getWorkoutCount() - 1) + postureScore;
        stats.setAvgPostureScore(totalScore / stats.getWorkoutCount());

        dailyStatsRepository.save(stats);
    }

    @Transactional
    public void updateStreakAfterWorkout(User user, LocalDate workoutDate) {
        UserStreak streak = userStreakRepository.findByUserId(user.getId())
                .orElseGet(() -> UserStreak.builder()
                        .user(user)
                        .currentStreak(0)
                        .longestStreak(0)
                        .lastWorkoutDate(null)
                        .build());

        LocalDate lastDate = streak.getLastWorkoutDate();

        if (lastDate == null) {
            streak.setCurrentStreak(1);
        } else if (lastDate.equals(workoutDate)) {
            // Same day, no streak increment
        } else if (lastDate.equals(workoutDate.minusDays(1))) {
            streak.setCurrentStreak(streak.getCurrentStreak() + 1);
        } else {
            // Broken streak, reset
            streak.setCurrentStreak(1);
        }

        if (streak.getCurrentStreak() > streak.getLongestStreak()) {
            streak.setLongestStreak(streak.getCurrentStreak());
        }

        streak.setLastWorkoutDate(workoutDate);
        userStreakRepository.save(streak);
    }

    @Transactional
    public void updatePersonalRecord(User user, Exercise exercise, int reps, int durationSec) {
        PersonalRecord record = personalRecordRepository.findByUserIdAndExerciseId(user.getId(), exercise.getId())
                .orElseGet(() -> PersonalRecord.builder()
                        .user(user)
                        .exercise(exercise)
                        .maxReps(0)
                        .maxDurationSeconds(0)
                        .achievedAt(LocalDateTime.now())
                        .build());

        boolean updated = false;
        if (reps > record.getMaxReps()) {
            record.setMaxReps(reps);
            updated = true;
        }
        if (durationSec > record.getMaxDurationSeconds()) {
            record.setMaxDurationSeconds(durationSec);
            updated = true;
        }

        if (updated) {
            record.setAchievedAt(LocalDateTime.now());
            personalRecordRepository.save(record);
        }
    }
}
