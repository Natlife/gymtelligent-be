package prm.projectbase.controller;

import prm.projectbase.dto.request.UserProfileRequest;
import prm.projectbase.dto.response.BaseResponse;
import prm.projectbase.dto.response.UserProfileResponse;
import prm.projectbase.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users/me/profile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public BaseResponse<UserProfileResponse> getMyProfile() {
        UserProfileResponse response = userProfileService.getProfile(getCurrentUserId());
        return BaseResponse.success(response, "Fetched profile successfully");
    }

    @PutMapping
    public BaseResponse<UserProfileResponse> updateMyProfile(@RequestBody @Valid UserProfileRequest request) {
        UserProfileResponse response = userProfileService.createOrUpdateProfile(getCurrentUserId(), request);
        return BaseResponse.success(response, "Profile updated successfully");
    }
}
