package feelingsapp.customerapp.entity;

import java.util.UUID;

public record ProductReview(UUID id, int productId, int rating , String review) {
}
