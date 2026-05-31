package prm.projectbase.controller;

import prm.projectbase.dto.response.BaseResponse;
import prm.projectbase.dto.response.DailyStatsResponse;
import prm.projectbase.dto.response.StatsSummaryResponse;
import prm.projectbase.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/summary")
    public BaseResponse<StatsSummaryResponse> getSummary() {
        StatsSummaryResponse response = statsService.getSummaryStats(getCurrentUserId());
        return BaseResponse.success(response, "Fetched summary stats successfully");
    }

    @GetMapping("/daily")
    public BaseResponse<DailyStatsResponse> getDailyStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        DailyStatsResponse response = statsService.getDailyStats(getCurrentUserId(), date);
        return BaseResponse.success(response, "Fetched daily stats successfully");
    }

    @GetMapping("/weekly")
    public BaseResponse<List<DailyStatsResponse>> getWeeklyStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        List<DailyStatsResponse> response = statsService.getWeeklyStats(getCurrentUserId(), weekStart);
        return BaseResponse.success(response, "Fetched weekly stats successfully");
    }

    @GetMapping("/monthly")
    public BaseResponse<List<DailyStatsResponse>> getMonthlyStats(
            @RequestParam int year,
            @RequestParam int month) {
        List<DailyStatsResponse> response = statsService.getMonthlyStats(getCurrentUserId(), year, month);
        return BaseResponse.success(response, "Fetched monthly stats successfully");
    }
}
