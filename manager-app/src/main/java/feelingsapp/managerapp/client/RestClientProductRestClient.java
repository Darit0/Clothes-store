package feelingsapp.managerapp.client;

import feelingsapp.managerapp.controller.payload.NewProductPayload;
import feelingsapp.managerapp.controller.payload.UpdateProductPayload;
import feelingsapp.managerapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductRestClient implements ProductRestClient{

    private final static ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE=
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;
    @Override
    public List<Product> findAllProducts() {
        return this.restClient
                .get()
                .uri("/store-api/products")
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return this.restClient
                    .post()
                    .uri("/store-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(title, details))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/store-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        }catch (HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String details) {
        try {
            this.restClient
                    .patch()
                    .uri("/store-api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(title, details))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));

        }
    }

    @Override
    public void deleteProduct(int productId) {
            try {
                this.restClient
                        .delete()
                        .uri("/store-api/products/{productId}", productId)
                        .retrieve()
                        .toBodilessEntity();
            }catch (HttpClientErrorException.NotFound exception){
                throw new NoSuchElementException(exception);
            }
    }

}

