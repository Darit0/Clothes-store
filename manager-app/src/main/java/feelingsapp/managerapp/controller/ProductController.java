package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.controller.payload.UpdateProductPayload;
import feelingsapp.managerapp.entity.Product;
import feelingsapp.managerapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("store/products/{productId:\\d+}")
public class ProductController {

    private final ProductService productService;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId){
        return this.productService.findProduct(productId).orElseThrow();
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
    public String updateProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload){
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/store/products/%d".formatted(product.getId());
    }
}
