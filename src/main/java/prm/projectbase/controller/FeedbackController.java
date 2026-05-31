package prm.projectbase.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prm.projectbase.dto.request.FeedbackRequest;
import prm.projectbase.dto.response.BaseResponse;
import prm.projectbase.dto.response.FeedbackResponse;
import prm.projectbase.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {

    FeedbackService feedbackService;

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public BaseResponse<FeedbackResponse> submitFeedback(@RequestBody @Valid FeedbackRequest request) {
        FeedbackResponse response = feedbackService.submitFeedback(getCurrentUserId(), request);
        return BaseResponse.success(response, "Feedback submitted successfully");
    }

    @GetMapping
    public BaseResponse<List<FeedbackResponse>> getAllFeedbacks() {
        List<FeedbackResponse> response = feedbackService.getAllFeedbacks();
        return BaseResponse.success(response, "Fetched all feedbacks successfully");
    }
}
