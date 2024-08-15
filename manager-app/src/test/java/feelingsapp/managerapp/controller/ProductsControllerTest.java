package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.client.BadRequestException;
import feelingsapp.managerapp.client.ProductRestClient;
import feelingsapp.managerapp.controller.payload.NewProductPayload;
import feelingsapp.managerapp.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductsController")
class ProductsControllerTest {


    @Mock
    ProductRestClient productRestClient ;

    @InjectMocks
    StoreController controller ;

    @Test
    @DisplayName("createProduct создаст товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage(){
        // given
        var payload = new NewProductPayload("Новый товар","Описание нового товара");
        var model = new ConcurrentModel();

        doReturn(new Product(1,"Новый товар","Описание нового товара"))
                .when(this.productRestClient)
                .createProduct("Новый товар","Описание нового товара");

        //when
        var result = this.controller.createProduct(payload, model);

        //then
        assertEquals("redirect:/store/products/1", result);

        verify(this.productRestClient).createProduct("Новый товар","Описание нового товара");
        verifyNoMoreInteractions(this.productRestClient);
    }

    @Test
    @DisplayName("createProduct вернет страницу с ошибками, если запрос не валиден")
    void creteProduct_RequestIsInvalid_ReturnsProductFormWithErrors(){

        //given

        var payload = new NewProductPayload("     ",null);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.productRestClient)
                .createProduct("     ",null);
        //when
        var result = this.controller.createProduct(payload, model);

        //then
        assertEquals("store/products/new_products", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));

        verify(this.productRestClient).createProduct("     ",null);
        verifyNoMoreInteractions(this.productRestClient);
    }
}