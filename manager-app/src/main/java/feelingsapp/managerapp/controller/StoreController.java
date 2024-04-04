package feelingsapp.managerapp.controller;

import feelingsapp.managerapp.controller.payload.NewProductPayload;
import feelingsapp.managerapp.entity.Product;
import feelingsapp.managerapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

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
        public String createProduct(@Valid NewProductPayload payload,
                                    BindingResult bindingResult ,
                                    Model model){
            if (bindingResult.hasErrors()){
                model.addAttribute("payload",payload);
                model.addAttribute("errors", bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList());
                return "store/products/new_products";
            }else{
                Product product = this.productService.createProduct(payload.title(), payload.details());
                return "redirect:/store/products/%d".formatted(product.getId());}
            }



}



