package feelingsapp.managerapp.repository;

import feelingsapp.managerapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(Integer productId);
}
