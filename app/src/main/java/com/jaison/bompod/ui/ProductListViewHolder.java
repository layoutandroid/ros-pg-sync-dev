package com.jaison.bompod.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaison.bompod.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaison on 12/10/17.
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.totalQuantity)
    TextView totalQuantity;
    @BindView(R.id.totalQuantitySold)
    TextView totalQuantitySold;

    public ProductListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
