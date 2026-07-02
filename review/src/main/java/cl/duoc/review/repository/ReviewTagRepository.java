package cl.duoc.review.repository;

import cl.duoc.review.model.Review;
import cl.duoc.review.model.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, UUID> {
    List<ReviewTag> findByReview(Review review);
}
