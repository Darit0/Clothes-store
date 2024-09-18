package feelingsapp.feedbackservice.controller;

import feelingsapp.feedbackservice.entity.ProductReview;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Slf4j
@SpringBootTest
@AutoConfigureWebClient
class ProductReviewsRestControllerIT {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setUp() {
        this.reactiveMongoTemplate.insertAll(List.of(
                        new ProductReview(UUID.fromString("bd7779c2-cb05-11ee-b5f3-df46a1249898"),
                                1, 1, "Отзыв1", "user1"),
                        new ProductReview(UUID.fromString("be424abc-cb05-11ee-ab16-2b747e61f570"),
                                1, 3, "Отзыв2", "user2"),
                        new ProductReview(UUID.fromString("be77f95a-cb05-11ee-91a3-1bdc94fa9de4"),
                                1, 5, "Отзыв3", "user3")
                ))
                .blockLast();
    }

    @AfterEach
    void tearDown(){
        this.reactiveMongoTemplate.remove(ProductReview.class)
                .all()
                .block();
    }

    @Test
    void findProductReviewsByProductId_ReturnsProductReviews() {
        //when

        //then
        this.webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .json("""
[
                        {"id":"bd7779c2-cb05-11ee-b5f3-df46a1249898",
                        "productId": 1, 
                        "rating":1,
                        "review": "Отзыв1",
                         "userId":"user1"},
                         
                        {"id":"be424abc-cb05-11ee-ab16-2b747e61f570",
                        "productId": 1,
                         "rating":3,
                        "review": "Отзыв2",
                         "userId":"user2"},
                         
                        {"id":"be77f95a-cb05-11ee-91a3-1bdc94fa9de4",
                        "productId": 1, 
                        "rating":5,
                        "review": "Отзыв3",
                         "userId":"user3"}

                      ]""");
    }

    @Test
    void createProductReview_RequestIsValid_ReturnsCreatedProductReview() {
        //given

        //when
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "productId": 1,
                            "rating": 5,
                            "review": "extremely slay"
                        }""")
        //then
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        {
                            "productId": 1,
                            "rating": 5,
                            "review": "extremely slay",
                            "userId": "user-tester"
                        }""").jsonPath("$.id").exists();
    }

    @Test
    void createProductReview_RequestIsInValid_ReturnsBadRequest() {
        //given

        //when
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "productId": null,
                            "rating": -1,
                            "review": "extremely slay"
                        }""")
                //then
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader().doesNotExist(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .json("""
                        {
                            errors: [
                            "ID введен неверно",
                            "Оценка меньше 1"
    ]
                        }""");
    }

    @Test
    void findProductReviewsByProductId_UserIsNotAuthenticated_ReturnsNotAuthenticated() {
        //when

        //then
        this.webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void createProductReview_UserIsNotAuthenticated_ReturnsNotAuthorized() {
        // given

        // when
        this.webTestClient
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "productId": 1,
                            "rating": 5,
                            "review": "extremely slay"
                        }""")
                .exchange()
                // then
                .expectStatus().isUnauthorized();
    }
}
