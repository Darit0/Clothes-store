package feelingsapp.storeservice.controller;

import feelingsapp.storeservice.controller.payload.NewProductPayload;
import feelingsapp.storeservice.entity.Product;
import feelingsapp.storeservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter) {

        return this.productService.findAllProducts(filter);
    }

//    @PostMapping
//    @Operation(
//            requstBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(
//                                    type = "object",
//                                    properties = {
//                                            @StringToClassMapItem(key = "title", value = String.class),
//                                            @StringToClassMapItem(key = "details", value = String.class)
//                                    }
//                            )
//                    )
//            ),
//            responses = {
//            @ApiResponse(
//                    responseCode = "201",
//                    headers = @Header(name = "Content-Type", description = "Тип данных"),
//                    content = {@Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(
//                                    type = "object",
//                                    properties = {
//                                            @StringToClassMapItem(key = "id", value = Integer.class),
//                                            @StringToClassMapItem(key = "title", value = String.class),
//                                            @StringToClassMapItem(key = "details", value = String.class)
//                                    }
//                            ))}
//            )
//    })
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/store-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}