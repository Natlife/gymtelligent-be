package prm.projectbase.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    Double weightKg;
    Double heightCm;
    Integer age;
    String gender;      // MALE, FEMALE, OTHER
    String fitnessGoal;  // BUILD_MUSCLE, LOSE_FAT, IMPROVE_STRENGTH, GENERAL_FITNESS
    String fitnessLevel; // BEGINNER, INTERMEDIATE, ADVANCED
    String avatarUrl;
}
