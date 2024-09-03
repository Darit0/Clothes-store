package feelingsapp.feedbackservice.controller;

import feelingsapp.feedbackservice.controller.payload.NewProductReviewPayload;
import feelingsapp.feedbackservice.entity.ProductReview;
import feelingsapp.feedbackservice.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("feedback-api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewsRestController {

    private final ProductReviewsService productReviewsService;

    @GetMapping("by-product-id/{productId:\\d+}")
    public Flux<ProductReview> findProductReviewsByProductId(@PathVariable("productId") int productId) {
        return this.productReviewsService.findProductReviewsByProduct(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            @Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {

        return payloadMono
                .flatMap(payload -> this.productReviewsService.createProductReview(payload.productId(),
                        payload.rating(), payload.reviews()))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/product-reviews/{id}")
                                .build(productReview.getId()))
                        .body(productReview));

    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleWebExchangeBindException(WebExchangeBindException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                problemDetail.setProperty("errors", exception.getAllErrors().stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .toList());
                return Mono.just(ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                        .body(problemDetail));
    }
}
