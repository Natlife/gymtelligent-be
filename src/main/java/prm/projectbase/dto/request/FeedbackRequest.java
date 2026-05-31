package prm.projectbase.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    @NotBlank(message = "Title cannot be blank")
    String title;

    @NotBlank(message = "Content cannot be blank")
    String content;
}
