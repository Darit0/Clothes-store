package feelingsapp.customerapp.config;

import feelingsapp.customerapp.client.WebClientFavouriteProductsClient;
import feelingsapp.customerapp.client.WebClientProductReviewsClient;
import feelingsapp.customerapp.client.WebClientProductsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder storeServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .filter(filter);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${clothes-store.services.store.uri:http://localhost:8081}") String storeBaseUri,
            WebClient.Builder storeServicesWebClientBuilder
    ) {
        return new WebClientProductsClient(storeServicesWebClientBuilder
                .baseUrl(storeBaseUri)
                .build());
    }

    @Bean
    public WebClientFavouriteProductsClient webClientFavouriteProductsClient(
            @Value("${clothes-store.services.feedback.uri:http://localhost:8085}") String feedbackBaseUri,
            WebClient.Builder storeServicesWebClientBuilder
    ) {
        return new WebClientFavouriteProductsClient(storeServicesWebClientBuilder
                .baseUrl(feedbackBaseUri)
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${clothes-store.services.feedback.uri:http://localhost:8085}") String feedbackBaseUrl,
            WebClient.Builder storeServicesWebClientBuilder
    ) {
        return new WebClientProductReviewsClient(storeServicesWebClientBuilder
                .baseUrl(feedbackBaseUrl)
                .build());
    }

}
