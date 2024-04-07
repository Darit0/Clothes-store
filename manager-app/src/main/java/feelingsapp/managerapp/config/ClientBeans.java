package feelingsapp.managerapp.config;


import feelingsapp.managerapp.client.RestClientProductRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductRestClient productRestClient
            (@Value("${clothes-store.service.store.uri:http://localhost:8081}") String storeBaseUri){
        return new RestClientProductRestClient(RestClient.builder()
                .baseUrl(storeBaseUri)
                .build());
    }

}
