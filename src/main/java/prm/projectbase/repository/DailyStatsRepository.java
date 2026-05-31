package prm.projectbase.repository;

import prm.projectbase.entity.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, Integer> {
    Optional<DailyStats> findByUserIdAndStatDate(Integer userId, LocalDate statDate);
    List<DailyStats> findByUserIdAndStatDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);
}
