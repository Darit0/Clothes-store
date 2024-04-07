package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.client.BadRequestException;
import feelingsapp.managerapp.client.ProductRestClient;
import feelingsapp.managerapp.controller.payload.NewProductPayload;
import feelingsapp.managerapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("store/products")
public class StoreController {

    private final ProductRestClient productRestClient;

    @GetMapping("/list")
    public String getProductList(Model model) {
        model.addAttribute("products", this.productRestClient.findAllProducts());
        return "store/products/list";

    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "store/products/new_products";
    }

    @PostMapping("/create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = this.productRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/store/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "store/products/new_products";
        }
    }
}


