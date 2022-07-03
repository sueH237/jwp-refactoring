package kitchenpos.product.ui.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductCreateResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductCreateResponse() {
    }

    public ProductCreateResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice().value();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
