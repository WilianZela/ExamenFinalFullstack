package cl.duoc.review.service;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewRequestDTO;
import cl.duoc.review.dto.ReviewResponseDTO;
import cl.duoc.review.dto.UserDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final DestinationService destinationService;

    public ApiResponse<ReviewResponseDTO> createReview(String token, ReviewRequestDTO dto) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            logger.warn("Token inválido al crear reseña");
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        ApiResponse<Boolean> destResponse = destinationService.validateDestination(dto.getDestinationId(), token);
        if (destResponse == null || destResponse.getCode() != 200 || destResponse.getData() == null || !destResponse.getData()) {
            logger.warn("Destino no válido: {}", dto.getDestinationId());
            return new ApiResponse<>(400, "Destino inválido o no encontrado", null);
        }

        Review review = new Review();
        review.setDestinationId(dto.getDestinationId());
        review.setUserId(user.getId());
        review.setUsername(user.getUsername());
        review.setPlaceName(dto.getPlaceName());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);
        logger.info("Reseña creada con ID: {}", saved.getId());
        return new ApiResponse<>(200, "Reseña creada correctamente", toResponse(saved));
    }

    public ApiResponse<ReviewResponseDTO> getReviewById(String token, UUID id) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        return reviewRepository.findById(id)
                .map(r -> new ApiResponse<>(200, "Reseña encontrada", toResponse(r)))
                .orElse(new ApiResponse<>(404, "Reseña no encontrada", null));
    }

    public ApiResponse<List<ReviewResponseDTO>> getAllReviews(String token) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        List<ReviewResponseDTO> reviews = reviewRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Listado de reseñas", reviews);
    }

    public ApiResponse<List<ReviewResponseDTO>> getReviewsByDestination(String token, UUID destinationId) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        List<ReviewResponseDTO> reviews = reviewRepository.findByDestinationId(destinationId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Reseñas del destino", reviews);
    }

    public ApiResponse<List<ReviewResponseDTO>> getReviewsByUser(String token) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();
        List<ReviewResponseDTO> reviews = reviewRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Reseñas del usuario", reviews);
    }

    public ApiResponse<ReviewResponseDTO> updateReview(String token, UUID id, ReviewRequestDTO dto) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        return reviewRepository.findById(id)
                .map(review -> {
                    if (!review.getUserId().equals(user.getId())) {
                        return new ApiResponse<ReviewResponseDTO>(403, "No tiene permisos para editar esta reseña", null);
                    }
                    review.setPlaceName(dto.getPlaceName());
                    review.setRating(dto.getRating());
                    review.setComment(dto.getComment());
                    Review updated = reviewRepository.save(review);
                    logger.info("Reseña actualizada: {}", updated.getId());
                    return new ApiResponse<>(200, "Reseña actualizada correctamente", toResponse(updated));
                })
                .orElse(new ApiResponse<>(404, "Reseña no encontrada", null));
    }

    public ApiResponse<Void> deleteReview(String token, UUID id) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse == null || authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        return reviewRepository.findById(id)
                .map(review -> {
                    if (!review.getUserId().equals(user.getId())) {
                        return new ApiResponse<Void>(403, "No tiene permisos para eliminar esta reseña", null);
                    }
                    reviewRepository.deleteById(id);
                    logger.info("Reseña eliminada: {}", id);
                    return new ApiResponse<Void>(200, "Reseña eliminada correctamente", null);
                })
                .orElse(new ApiResponse<>(404, "Reseña no encontrada", null));
    }

    private ReviewResponseDTO toResponse(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getDestinationId(),
                review.getUserId(),
                review.getUsername(),
                review.getPlaceName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}