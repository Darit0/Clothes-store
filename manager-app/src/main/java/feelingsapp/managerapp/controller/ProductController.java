package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.client.BadRequestException;
import feelingsapp.managerapp.client.ProductRestClient;
import feelingsapp.managerapp.controller.payload.UpdateProductPayload;
import feelingsapp.managerapp.entity.Product;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("store/products/{productId:\\d+}")
public class ProductController {

    private final ProductRestClient productRestClient;

    private  final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId){
        return this.productRestClient.findProduct(productId)
                .orElseThrow(()->new NoSuchElementException("store.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct(){
        return "store/products/product";
    }

    @GetMapping("/edit")
    public String getProductEditPage(){
        return "store/products/edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product,
                                UpdateProductPayload payload,
                                Model model,
                                HttpServletResponse response) {
        try {
            this.productRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("product") Product product){
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/store/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handlerNoSuchElementException(NoSuchElementException exception, Model model,
                                                HttpServletResponse response, Locale locale){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        return "errors/404";
    }
}
