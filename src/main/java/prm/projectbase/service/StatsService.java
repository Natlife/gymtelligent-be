package prm.projectbase.service;

import prm.projectbase.entity.*;
import prm.projectbase.repository.DailyStatsRepository;
import prm.projectbase.repository.UserStreakRepository;
import prm.projectbase.dto.response.DailyStatsResponse;
import prm.projectbase.dto.response.StatsSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsService {

    DailyStatsRepository dailyStatsRepository;
    UserStreakRepository userStreakRepository;

    @Transactional(readOnly = true)
    public StatsSummaryResponse getSummaryStats(Integer userId) {
        // Fetch streak
        UserStreak streak = userStreakRepository.findByUserId(userId)
                .orElse(UserStreak.builder().currentStreak(0).longestStreak(0).build());

        // We can fetch all daily_stats to sum up total workouts and total calories
        List<DailyStats> allStats = dailyStatsRepository.findAll(); // Simple find all for user, but we can query specific later if needed
        double totalCalories = 0.0;
        int totalWorkouts = 0;

        for (DailyStats stat : allStats) {
            if (stat.getUser() != null && stat.getUser().getId() == userId) {
                totalCalories += stat.getTotalCalories();
                totalWorkouts += stat.getWorkoutCount();
            }
        }

        return StatsSummaryResponse.builder()
                .totalWorkouts(totalWorkouts)
                .totalCalories(totalCalories)
                .currentStreak(streak.getCurrentStreak())
                .longestStreak(streak.getLongestStreak())
                .build();
    }

    @Transactional(readOnly = true)
    public DailyStatsResponse getDailyStats(Integer userId, LocalDate date) {
        DailyStats stats = dailyStatsRepository.findByUserIdAndStatDate(userId, date)
                .orElse(DailyStats.builder()
                        .statDate(date)
                        .totalCalories(0.0)
                        .totalDurationSeconds(0)
                        .totalReps(0)
                        .workoutCount(0)
                        .avgPostureScore(0.0)
                        .build());

        return mapToResponse(stats);
    }

    @Transactional(readOnly = true)
    public List<DailyStatsResponse> getWeeklyStats(Integer userId, LocalDate weekStart) {
        List<DailyStatsResponse> weeklyList = new ArrayList<>();
        
        // Loop through 7 days to generate complete, high-quality stats even for days without logs
        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            Optional<DailyStats> statsOpt = dailyStatsRepository.findByUserIdAndStatDate(userId, date);
            
            if (statsOpt.isPresent()) {
                weeklyList.add(mapToResponse(statsOpt.get()));
            } else {
                weeklyList.add(DailyStatsResponse.builder()
                        .date(date)
                        .totalCalories(0.0)
                        .totalDurationSeconds(0)
                        .totalReps(0)
                        .workoutCount(0)
                        .avgPostureScore(0.0)
                        .build());
            }
        }
        return weeklyList;
    }

    @Transactional(readOnly = true)
    public List<DailyStatsResponse> getMonthlyStats(Integer userId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        
        List<DailyStats> statsList = dailyStatsRepository.findByUserIdAndStatDateBetween(userId, startOfMonth, endOfMonth);
        List<DailyStatsResponse> responseList = new ArrayList<>();
        
        for (DailyStats stat : statsList) {
            responseList.add(mapToResponse(stat));
        }
        return responseList;
    }

    private DailyStatsResponse mapToResponse(DailyStats stats) {
        return DailyStatsResponse.builder()
                .date(stats.getStatDate())
                .totalCalories(stats.getTotalCalories())
                .totalDurationSeconds(stats.getTotalDurationSeconds())
                .totalReps(stats.getTotalReps())
                .workoutCount(stats.getWorkoutCount())
                .avgPostureScore(stats.getAvgPostureScore())
                .build();
    }
}
