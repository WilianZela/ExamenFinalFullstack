package cl.duoc.review.controller;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewTagRequestDTO;
import cl.duoc.review.dto.ReviewTagResponseDTO;
import cl.duoc.review.service.ReviewTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Review Tags Controller", description = "Endpoints para gestión de etiquetas de reseñas")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review/tags")
public class ReviewTagController {

    private final ReviewTagService reviewTagService;

    @PostMapping
    @Operation(summary = "Agregar etiqueta a reseña", description = "Agrega una etiqueta descriptiva a una reseña existente.")
    public ResponseEntity<ApiResponse<ReviewTagResponseDTO>> addTag(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReviewTagRequestDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewTagResponseDTO> response = reviewTagService.addTag(token, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/review/{reviewId}")
    @Operation(summary = "Listar etiquetas de una reseña", description = "Retorna todas las etiquetas asociadas a una reseña específica.")
    public ResponseEntity<ApiResponse<List<ReviewTagResponseDTO>>> getTagsByReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID reviewId) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<ReviewTagResponseDTO>> response = reviewTagService.getTagsByReview(token, reviewId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar etiqueta", description = "Elimina una etiqueta existente por su UUID.")
    public ResponseEntity<ApiResponse<Void>> deleteTag(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<Void> response = reviewTagService.deleteTag(token, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}