package prm.projectbase.repository;

import prm.projectbase.entity.User;
import prm.projectbase.entity.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Integer> {
    Page<WorkoutSession> findByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = "exercise")
    Page<WorkoutSession> findByUserId(Integer userId, Pageable pageable);

    List<WorkoutSession> findByUserIdAndSessionDate(Integer userId, LocalDate sessionDate);
}
