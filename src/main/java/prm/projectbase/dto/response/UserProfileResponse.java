package prm.projectbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    Double weightKg;
    Double heightCm;
    Integer age;
    String gender;
    String fitnessGoal;
    String fitnessLevel;
    String avatarUrl;
    String fullName;
    String email;
    String roleName;
}
