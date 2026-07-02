package cl.duoc.review.dto;

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
public class ReviewTagRequestDTO {

    @NotNull(message = "El ID de la reseña es obligatorio")
    private UUID reviewId;

    @NotBlank(message = "El nombre de la etiqueta no puede estar vacío")
    @Size(min = 2, max = 100, message = "La etiqueta debe tener entre 2 y 100 caracteres")
    private String tagName;
}