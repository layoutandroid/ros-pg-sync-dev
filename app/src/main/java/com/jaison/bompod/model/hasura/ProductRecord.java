package com.jaison.bompod.model.hasura;

import com.google.gson.annotations.SerializedName;
import com.jaison.bompod.constant.RealmConfig;
import com.jaison.bompod.model.realm.Product;
import com.jaison.bompod.task.TaskConvertible;

/**
 * Created by jaison on 20/10/17.
 */

public class ProductRecord implements TaskConvertible<Product> {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    int price;

    @SerializedName("total_quantity")
    int totalQuantity;


    @Override
    public Product toTaskObject() {
        Product product = new Product(id, name, totalQuantity, price);
        return product;
    }

    @Override
    public String getRealmName() {
        return RealmConfig.URL.PRODUCT;
    }

    @Override
    public boolean shouldExecuteTask() {
        return true;
    }

    @Override
    public void onPostExecute() {

    }

    @Override
    public String toString() {
        return "ProductRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}
