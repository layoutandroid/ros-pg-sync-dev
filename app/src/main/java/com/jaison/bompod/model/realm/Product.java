package com.jaison.bompod.model.realm;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jaison on 12/10/17.
 */

public class Product extends RealmObject {

    @PrimaryKey
    public int id;

    public String name;
    public float price;
    public int totalQuantity = 0;
    public int totalQuantitySold = 0;

    public Product() {
    }

    public Product(int id, String name, int quantity, float price) {
        this.id = id;
        this.name = name;
        this.totalQuantity = quantity;
        this.totalQuantitySold = 0;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", totalQuantity=" + totalQuantity +
                ", totalQuantitySold=" + totalQuantitySold +
                '}';
    }
}
