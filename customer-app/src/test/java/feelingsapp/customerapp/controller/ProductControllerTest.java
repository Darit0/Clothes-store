package feelingsapp.customerapp.controller;

import feelingsapp.customerapp.client.FavouriteProductsClient;
import feelingsapp.customerapp.client.ProductReviewsClient;
import feelingsapp.customerapp.client.ProductsClient;
import feelingsapp.customerapp.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.ui.ConcurrentModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductsClient productsClient;

    @Mock
    FavouriteProductsClient favouriteProductsClient;

    @Mock
    ProductReviewsClient productReviewsClient;

    @InjectMocks
    ProductController controller ;

    @Test
    void loadProduct_ProductExists_ReturnsNotEmptyMono() {
        // given
        var product = new Product(1, "Old USSR", "Это по-русски");
        doReturn(Mono.just(product)).when(this.productsClient).findProduct(1);

        // when
        StepVerifier.create(this.controller.loadProduct(1))
                // then
                .expectNext(new Product(1, "Old USSR", "Это по-русски"))
                .verifyComplete();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favouriteProductsClient, this.productReviewsClient);
    }

    @Test
    void loadProduct_ProductDoesNotExists_ReturnsMonoWithNoSuchException() {
        //given

        doReturn(Mono.empty()).when(this.productsClient).findProduct(1);

        //when

        StepVerifier.create(this.controller.loadProduct(1))
                //then
                        .expectErrorMatches(exception -> {
                            return exception instanceof NoSuchElementException e &&
                                    e.getMessage().equals("customer.products.error.not_found"); })
                                        .verify();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoMoreInteractions(this.favouriteProductsClient, this.productReviewsClient);
    }


    @Test
    void handleNoSuchElementException_ReturnErrors404(){
        // given
        var exception = new NoSuchElementException("Не найдено");
        var model = new ConcurrentModel();
        var response = new MockServerHttpResponse();
        //when
        var result = this.controller.handleNoSuchElementException(exception, model, response);
        //then
        assertEquals("errors/404", result);
        assertEquals("Не найдено", model.getAttribute("error"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


}