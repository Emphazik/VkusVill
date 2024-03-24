package ru.vladushik.vkusvillstoremanagement.dao;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class ProductDao {
    private int id;
    private String name;
    private int price;
    private int count;
    private String category;
    private byte[] bytes;
    private Image image;
    private int weight;

    public ProductDao(int id, String name, int price, int count, String category, byte[] bytes, int weight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.category = category;
        this.bytes = bytes;
        this.image = new Image(new ByteArrayInputStream(this.bytes));
        this.weight = weight;
    }

    public ProductDao() {
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public IntegerProperty getWeightProperty() {
        return new SimpleIntegerProperty(weight);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public IntegerProperty getPriceProperty() {
        return new SimpleIntegerProperty(price);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public IntegerProperty getCountProperty() {
        return new SimpleIntegerProperty(count);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public StringProperty getCategoryProperty() {
        return new SimpleStringProperty(category);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ObjectProperty<Image> getImageProperty() {
        return new SimpleObjectProperty<>(image);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
