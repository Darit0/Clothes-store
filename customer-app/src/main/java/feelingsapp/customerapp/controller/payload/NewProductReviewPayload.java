package feelingsapp.customerapp.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload(
        @NotNull (message = "{customer.products.reviews.create.errors.rating_is_null}")
        @Min(value = 1 , message = "{customer.products.reviews.create.errors.rating_is_bellow_min}" )
        @Max(value = 5, message = "{customer.products.reviews.create.errors.rating_is_above_max}")
        Integer rating,
        @Size(max=1000, message = "{customer.products.reviews.create.errors.review_is_to_big}")
        String review) {


}
