package ru.vladushik.vkusvillstoremanagement.entity;

public class Product {
    private Integer id;
    private String name;
    private Integer price;
    private Integer count;
    private Category category;
    private byte[] image;

    public Product() {}

    public Product(Integer id, String name, Integer price, Integer count, Category category, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.category = category;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
