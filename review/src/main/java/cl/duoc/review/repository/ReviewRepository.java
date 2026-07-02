package cl.duoc.review.repository;

import cl.duoc.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByDestinationId(UUID destinationId);
    List<Review> findByUserId(UUID userId);
}
