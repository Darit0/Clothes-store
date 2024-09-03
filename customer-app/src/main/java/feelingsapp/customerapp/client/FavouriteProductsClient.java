package feelingsapp.customerapp.client;

import feelingsapp.customerapp.entity.FavouriteProduct;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




public interface FavouriteProductsClient {
    
    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId);

    Flux<FavouriteProduct> findFavouriteProducts();

    Mono<FavouriteProduct> addProductToFavourites(int productId);

    Mono<Void> removeProductFromFavourites(int productId);


}
