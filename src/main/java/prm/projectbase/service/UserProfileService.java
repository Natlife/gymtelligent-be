package prm.projectbase.service;

import prm.projectbase.entity.*;
import prm.projectbase.repository.UserProfileRepository;
import prm.projectbase.repository.UserRepository;
import prm.projectbase.dto.request.UserProfileRequest;
import prm.projectbase.dto.response.UserProfileResponse;
import prm.projectbase.exception.AppException;
import prm.projectbase.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {

    UserProfileRepository userProfileRepository;
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Integer userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                    return UserProfile.builder().user(user).build();
                });

        return mapToResponse(profile);
    }

    @Transactional
    public UserProfileResponse createOrUpdateProfile(Integer userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().user(user).build());

        if (request.getWeightKg() != null) profile.setWeightKg(request.getWeightKg());
        if (request.getHeightCm() != null) profile.setHeightCm(request.getHeightCm());
        if (request.getAge() != null) profile.setAge(request.getAge());
        if (request.getGender() != null) profile.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        if (request.getFitnessGoal() != null) profile.setFitnessGoal(FitnessGoal.valueOf(request.getFitnessGoal().toUpperCase()));
        if (request.getFitnessLevel() != null) profile.setFitnessLevel(FitnessLevel.valueOf(request.getFitnessLevel().toUpperCase()));
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .weightKg(profile.getWeightKg())
                .heightCm(profile.getHeightCm())
                .age(profile.getAge())
                .gender(profile.getGender() != null ? profile.getGender().name() : null)
                .fitnessGoal(profile.getFitnessGoal() != null ? profile.getFitnessGoal().name() : null)
                .fitnessLevel(profile.getFitnessLevel() != null ? profile.getFitnessLevel().name() : null)
                .avatarUrl(profile.getAvatarUrl())
                .fullName(profile.getUser() != null ? profile.getUser().getFullName() : null)
                .email(profile.getUser() != null ? profile.getUser().getEmail() : null)
                .roleName(profile.getUser() != null && profile.getUser().getRole() != null ? profile.getUser().getRole().getName() : "ROLE_USER")
                .build();
    }
}
