package cl.duoc.review.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "review_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewTag {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;
}
