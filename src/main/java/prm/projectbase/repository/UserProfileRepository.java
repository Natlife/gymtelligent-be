package prm.projectbase.repository;

import prm.projectbase.entity.User;
import prm.projectbase.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUserId(Integer userId);
}
