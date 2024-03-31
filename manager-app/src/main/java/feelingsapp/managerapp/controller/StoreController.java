package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.controller.payload.NewProductPayload;
import feelingsapp.managerapp.entity.Product;
import feelingsapp.managerapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("store/products")
public class StoreController {

        private final ProductService productService;

        @GetMapping("/list")
        public String getProductList(Model model){
            model.addAttribute("products", this.productService.findAllProducts());
            return "store/products/list";

        }

        @GetMapping("/create")
        public String getNewProductPage(){
            return "store/products/new_products";
        }

        @PostMapping("/create")
        public String createProduct(NewProductPayload payload){
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return "redirect:/store/products/list";}

        @GetMapping("{productId:\\d+}")
        public String getProduct(@PathVariable("productId") int productId, Model model){
            model.addAttribute("product",this.productService.findProduct(productId).orElseThrow());
            return "store/products/product";
        }










}



