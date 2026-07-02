package cl.duoc.review.service;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewTagRequestDTO;
import cl.duoc.review.dto.ReviewTagResponseDTO;
import cl.duoc.review.dto.UserDTO;
import cl.duoc.review.model.ReviewTag;
import cl.duoc.review.repository.ReviewRepository;
import cl.duoc.review.repository.ReviewTagRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewTagService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewTagService.class);

    private final ReviewTagRepository reviewTagRepository;
    private final ReviewRepository reviewRepository;
    private final AuthService authService;

    public ApiResponse<ReviewTagResponseDTO> addTag(String token, ReviewTagRequestDTO dto) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        return reviewRepository.findById(dto.getReviewId())
                .map(review -> {
                    ReviewTag tag = new ReviewTag();
                    tag.setReview(review);
                    tag.setTagName(dto.getTagName());
                    ReviewTag saved = reviewTagRepository.save(tag);
                    logger.info("Etiqueta agregada a reseña {}: {}", review.getId(), saved.getTagName());
                    return new ApiResponse<>(200, "Etiqueta agregada correctamente", toResponse(saved));
                })
                .orElse(new ApiResponse<>(404, "Reseña no encontrada", null));
    }

    public ApiResponse<List<ReviewTagResponseDTO>> getTagsByReview(String token, UUID reviewId) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        return reviewRepository.findById(reviewId)
                .map(review -> {
                    List<ReviewTagResponseDTO> tags = reviewTagRepository.findByReview(review)
                            .stream()
                            .map(this::toResponse)
                            .collect(Collectors.toList());
                    return new ApiResponse<>(200, "Etiquetas de la reseña", tags);
                })
                .orElse(new ApiResponse<>(404, "Reseña no encontrada", null));
    }

    public ApiResponse<Void> deleteTag(String token, UUID id) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        if (!reviewTagRepository.existsById(id)) {
            return new ApiResponse<>(404, "Etiqueta no encontrada", null);
        }

        reviewTagRepository.deleteById(id);
        logger.info("Etiqueta eliminada: {}", id);
        return new ApiResponse<>(200, "Etiqueta eliminada correctamente", null);
    }

    private ReviewTagResponseDTO toResponse(ReviewTag tag) {
        return new ReviewTagResponseDTO(
                tag.getId(),
                tag.getReview().getId(),
                tag.getTagName()
        );
    }
}