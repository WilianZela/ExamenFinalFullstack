package cl.duoc.review.controller;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewRequestDTO;
import cl.duoc.review.dto.ReviewResponseDTO;
import cl.duoc.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Reviews Controller", description = "Endpoints para gestión de reseñas de destinos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña asociada a un destino existente. Requiere token válido.")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReviewRequestDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewResponseDTO> response = reviewService.createReview(token, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID", description = "Retorna una reseña específica por su UUID.")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> getById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewResponseDTO> response = reviewService.getReviewById(token, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas las reseñas", description = "Retorna todas las reseñas registradas en el sistema.")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getAll(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<ReviewResponseDTO>> response = reviewService.getAllReviews(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/destination/{destinationId}")
    @Operation(summary = "Listar reseñas por destino", description = "Retorna todas las reseñas asociadas a un destino específico.")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getByDestination(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID destinationId) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<ReviewResponseDTO>> response = reviewService.getReviewsByDestination(token, destinationId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Listar reseñas del usuario autenticado", description = "Retorna todas las reseñas creadas por el usuario del token.")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getByUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<ReviewResponseDTO>> response = reviewService.getReviewsByUser(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente. Solo el autor puede editarla.")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> update(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequestDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewResponseDTO> response = reviewService.updateReview(token, id, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña existente. Solo el autor puede eliminarla.")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<Void> response = reviewService.deleteReview(token, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}