package cl.duoc.review;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewRequestDTO;
import cl.duoc.review.dto.ReviewResponseDTO;
import cl.duoc.review.dto.UserDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.repository.ReviewRepository;
import cl.duoc.review.service.AuthService;
import cl.duoc.review.service.DestinationService;
import cl.duoc.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AuthService authService;

    @Mock
    private DestinationService destinationService;

    @InjectMocks
    private ReviewService reviewService;

    private UserDTO mockUser;
    private ReviewRequestDTO validRequest;
    private Review savedReview;

    @BeforeEach
    void setUp() {
        mockUser = new UserDTO();
        mockUser.setId(UUID.randomUUID());
        mockUser.setUsername("juan.perez");

        validRequest = ReviewRequestDTO.builder()
                .destinationId(UUID.randomUUID())
                .placeName("Hotel Plaza")
                .rating(4)
                .comment("Muy buena atención")
                .build();

        savedReview = new Review();
        savedReview.setId(UUID.randomUUID());
        savedReview.setDestinationId(validRequest.getDestinationId());
        savedReview.setUserId(mockUser.getId());
        savedReview.setUsername(mockUser.getUsername());
        savedReview.setPlaceName(validRequest.getPlaceName());
        savedReview.setRating(validRequest.getRating());
        savedReview.setComment(validRequest.getComment());
        savedReview.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createReview_tokenValido_destinoValido_creaReseña() {
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(destinationService.validateDestination(any(), anyString()))
                .thenReturn(new ApiResponse<>(200, "Destino válido", true));
        when(reviewRepository.save(any())).thenReturn(savedReview);

        ApiResponse<ReviewResponseDTO> response = reviewService.createReview("tokenValido", validRequest);

        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        assertEquals("Hotel Plaza", response.getData().getPlaceName());
        assertEquals(4, response.getData().getRating());
        assertEquals("juan.perez", response.getData().getUsername());
    }

    @Test
    void createReview_tokenInvalido_retorna401() {
        when(authService.validateToken("tokenInvalido"))
                .thenReturn(new ApiResponse<>(401, "Token inválido", null));

        ApiResponse<ReviewResponseDTO> response = reviewService.createReview("tokenInvalido", validRequest);

        assertEquals(401, response.getCode());
        assertNull(response.getData());
    }

    @Test
    void createReview_destinoInvalido_retorna400() {
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(destinationService.validateDestination(any(), anyString()))
                .thenReturn(new ApiResponse<>(404, "Destino no encontrado", false));

        ApiResponse<ReviewResponseDTO> response = reviewService.createReview("tokenValido", validRequest);

        assertEquals(400, response.getCode());
        assertNull(response.getData());
    }

    @Test
    void getReviewById_existente_retornaReseña() {
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(reviewRepository.findById(savedReview.getId()))
                .thenReturn(Optional.of(savedReview));

        ApiResponse<ReviewResponseDTO> response = reviewService.getReviewById("tokenValido", savedReview.getId());

        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        assertEquals("Hotel Plaza", response.getData().getPlaceName());
    }

    @Test
    void getReviewById_noExiste_retorna404() {
        UUID idInexistente = UUID.randomUUID();
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(reviewRepository.findById(idInexistente))
                .thenReturn(Optional.empty());

        ApiResponse<ReviewResponseDTO> response = reviewService.getReviewById("tokenValido", idInexistente);

        assertEquals(404, response.getCode());
        assertNull(response.getData());
    }

    @Test
    void getAllReviews_retornaListaCompleta() {
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(reviewRepository.findAll()).thenReturn(List.of(savedReview));

        ApiResponse<List<ReviewResponseDTO>> response = reviewService.getAllReviews("tokenValido");

        assertEquals(200, response.getCode());
        assertEquals(1, response.getData().size());
        assertEquals("Hotel Plaza", response.getData().get(0).getPlaceName());
    }

    @Test
    void deleteReview_propietario_eliminaCorrectamente() {
        when(authService.validateToken("tokenValido"))
                .thenReturn(new ApiResponse<>(200, "Token válido", mockUser));
        when(reviewRepository.findById(savedReview.getId()))
                .thenReturn(Optional.of(savedReview));

        ApiResponse<Void> response = reviewService.deleteReview("tokenValido", savedReview.getId());

        assertEquals(200, response.getCode());
    }

    @Test
    void deleteReview_usuarioNoAutorizado_retorna403() {
        UserDTO otroUsuario = new UserDTO();
        otroUsuario.setId(UUID.randomUUID());
        otroUsuario.setUsername("otro.usuario");

        when(authService.validateToken("tokenOtro"))
                .thenReturn(new ApiResponse<>(200, "Token válido", otroUsuario));
        when(reviewRepository.findById(savedReview.getId()))
                .thenReturn(Optional.of(savedReview));

        ApiResponse<Void> response = reviewService.deleteReview("tokenOtro", savedReview.getId());

        assertEquals(403, response.getCode());
    }
}
