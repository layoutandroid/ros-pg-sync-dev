package com.jaison.bompod.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaison.bompod.R;
import com.jaison.bompod.manager.RealmManager;
import com.jaison.bompod.model.realm.Product;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jaison on 12/10/17.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {

    RealmResults<Product> products;

    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_details, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductListViewHolder holder, final int position) {
        final Product product = products.get(position);
        holder.name.setText("Name: " + product.name);
        holder.totalQuantity.setText("Total Quantity: " + product.totalQuantity);
        holder.totalQuantitySold.setText("Total Quantity Sold: " + product.totalQuantitySold);
    }

    public void setProducts(RealmResults<Product> products) {
        this.products = products;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }
}
