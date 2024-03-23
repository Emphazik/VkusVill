package ru.vladushik.vkusvillstoremanagement.dao;

import javafx.scene.image.Image;

public class ProductDao {
    private int id;
    private String name;
    private int price;
    private int count;
    private String category;
    private Image image;

    public ProductDao(int id, String name, int price, int count, String category, Image image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.category = category;
        this.image = image;
    }

    public ProductDao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
