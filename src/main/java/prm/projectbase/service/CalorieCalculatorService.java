package prm.projectbase.service;

import org.springframework.stereotype.Service;

@Service
public class CalorieCalculatorService {

    /**
     * Calculates calories burned using MET formula:
     * Calories/minute = (MET * 3.5 * weightKg) / 200
     * Total Calories = Calories/minute * (durationSeconds / 60.0)
     */
    public double calculateCalories(double metValue, double weightKg, int durationSeconds) {
        double durationMinutes = durationSeconds / 60.0;
        return (metValue * 3.5 * weightKg) / 200.0 * durationMinutes;
    }

    /**
     * Estimated seconds per rep for fallback when timer is not available:
     * Push Ups: ~3s per rep
     * Squats: ~3.5s per rep
     * Deadlifts: ~5.0s per rep
     */
    public double estimateSecondsPerRep(String exerciseName) {
        if (exerciseName == null) return 3.0;
        String lower = exerciseName.toLowerCase();
        if (lower.contains("push")) {
            return 3.0;
        } else if (lower.contains("squat")) {
            return 3.5;
        } else if (lower.contains("deadlift")) {
            return 5.0;
        }
        return 3.0; // Default fallback
    }

    public double calculateCaloriesByReps(double metValue, double weightKg, int totalReps, String exerciseName) {
        double secondsPerRep = estimateSecondsPerRep(exerciseName);
        int estimatedDuration = (int) (totalReps * secondsPerRep);
        return calculateCalories(metValue, weightKg, estimatedDuration);
    }
}
