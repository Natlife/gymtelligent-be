package prm.projectbase.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prm.projectbase.dto.request.FeedbackRequest;
import prm.projectbase.dto.response.FeedbackResponse;
import prm.projectbase.entity.Feedback;
import prm.projectbase.entity.User;
import prm.projectbase.exception.AppException;
import prm.projectbase.exception.ErrorCode;
import prm.projectbase.repository.FeedbackRepository;
import prm.projectbase.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    UserRepository userRepository;

    @Transactional
    public FeedbackResponse submitFeedback(Integer userId, FeedbackRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Feedback feedback = Feedback.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .senderName(feedback.getUser().getFullName() != null ? feedback.getUser().getFullName() : feedback.getUser().getUserName())
                .senderEmail(feedback.getUser().getEmail())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
