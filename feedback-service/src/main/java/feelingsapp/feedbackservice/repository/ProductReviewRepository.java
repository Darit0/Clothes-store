package feelingsapp.feedbackservice.repository;

import feelingsapp.feedbackservice.entity.ProductReview;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductReviewRepository extends ReactiveCrudRepository<ProductReview, UUID> {

    Flux<ProductReview> findAllByProductId(int productId);
}