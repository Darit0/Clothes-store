package feelingsapp.managerapp.service;

import feelingsapp.managerapp.entity.Product;
import feelingsapp.managerapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts(){
        return this.productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String details){
        return this.productRepository.save(new Product(null,title,details));
    }

    @Override
    public Optional<Product> findProduct(int productId){
        return this.productRepository.findById(productId);
    }

}
