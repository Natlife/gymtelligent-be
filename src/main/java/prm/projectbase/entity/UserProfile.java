package prm.projectbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "weight_kg")
    Double weightKg;

    @Column(name = "height_cm")
    Double heightCm;

    Integer age;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_goal")
    FitnessGoal fitnessGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_level")
    FitnessLevel fitnessLevel;

    @Column(name = "avatar_url")
    String avatarUrl;
}
