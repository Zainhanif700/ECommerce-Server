package com.zain.Request;

public class AddItemRequest {
    private Long productId;
    private String size;
    private int quantity;
    private Integer price;

    @Override
    public String toString() {
        return "AddItemRequest{" +
                "productId=" + productId +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    public AddItemRequest() {
    }

    public AddItemRequest(Long productId, String size, int quantity, Integer price) {
        this.productId = productId;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
