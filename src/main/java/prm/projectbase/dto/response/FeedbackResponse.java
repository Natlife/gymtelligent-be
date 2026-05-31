package prm.projectbase.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
    int id;
    String senderName;
    String senderEmail;
    String title;
    String content;
    LocalDateTime createdAt;
}
