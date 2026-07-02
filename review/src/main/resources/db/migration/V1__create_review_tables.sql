CREATE TABLE reviews (
    id BINARY(16) NOT NULL,
    destination_id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    username VARCHAR(100) NOT NULL,
    place_name VARCHAR(200) NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5)
);

CREATE TABLE review_tags (
    id BINARY(16) NOT NULL,
    review_id BINARY(16) NOT NULL,
    tag_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_review_tag FOREIGN KEY (review_id) REFERENCES reviews(id)
);