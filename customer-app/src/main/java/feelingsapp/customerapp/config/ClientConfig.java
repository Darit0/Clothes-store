package feelingsapp.customerapp.config;

import feelingsapp.customerapp.client.WebClientFavouriteProductsClient;
import feelingsapp.customerapp.client.WebClientProductReviewsClient;
import feelingsapp.customerapp.client.WebClientProductsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${clothes-store.services.store.uri:http://localhost:8081}") String storeBaseUri
    ) {
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl(storeBaseUri)
                .build());
    }

    @Bean
    public WebClientFavouriteProductsClient webClientFavouriteProductsClient(
            @Value("${clothes-store.services.feedback.uri:http://localhost:8085}") String feedbackBaseUri
    ) {
        return new WebClientFavouriteProductsClient(WebClient.builder()
                .baseUrl(feedbackBaseUri)
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${clothes-store.services.feedback.uri:http://localhost:8085}") String feedbackBaseUrl
    ) {
        return new WebClientProductReviewsClient(WebClient.builder()
                .baseUrl(feedbackBaseUrl)
                .build());
    }

}
