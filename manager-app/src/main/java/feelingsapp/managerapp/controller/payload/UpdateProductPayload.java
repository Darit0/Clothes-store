package feelingsapp.managerapp.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductPayload(
        @NotNull( message = "{store.products.update.errors.title_is_null}")
        @Size(min=3,max=50, message = "{store.products.update.errors.title_size_is_invalid}")
        String title,
        @Size(max=1000, message = "{store.products.create.update.details_size_is_invalid}")
        String details){
}
