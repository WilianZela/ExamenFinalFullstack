package cl.duoc.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewTagResponseDTO {
    private UUID id;
    private UUID reviewId;
    private String tagName;
}