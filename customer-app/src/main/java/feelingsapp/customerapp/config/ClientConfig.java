package feelingsapp.customerapp.config;

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
}
