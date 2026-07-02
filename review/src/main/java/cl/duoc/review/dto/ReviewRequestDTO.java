package cl.duoc.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {

    @NotNull(message = "El ID del destino es obligatorio")
    private UUID destinationId;

    @NotBlank(message = "El nombre del lugar no puede estar vacío")
    @Size(min = 3, max = 200, message = "El nombre del lugar debe tener entre 3 y 200 caracteres")
    private String placeName;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comment;
}