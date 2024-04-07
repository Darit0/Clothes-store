package feelingsapp.storeservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull(  message = "{store.products.create.errors.title_is_null}")
        @Size(min=3,max=50, message = "{store.products.create.errors.title_size_is_invalid}")
        String title,
        @Size(max=1000,  message = "{store.products.create.errors.details_size_is_invalid}")
        String details) {
}
