package cl.duoc.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private UUID id;
    private UUID destinationId;
    private UUID userId;
    private String username;
    private String placeName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}