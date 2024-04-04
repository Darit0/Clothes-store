package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.controller.payload.UpdateProductPayload;
import feelingsapp.managerapp.entity.Product;
import feelingsapp.managerapp.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("store/products/{productId:\\d+}")
public class ProductController {

    private final ProductService productService;

    private  final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId){
        return this.productService.findProduct(productId)
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
    public String updateProduct(@ModelAttribute(name="product", binding = false) Product product,
                                @Valid UpdateProductPayload payload,
                                BindingResult bindingResult ,
                                Model model){

        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "store/products/edit";
        }else{
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/store/products/%d".formatted(product.getId());
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@ModelAttribute("product") Product product){
        this.productService.deleteProduct(product.getId());
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
