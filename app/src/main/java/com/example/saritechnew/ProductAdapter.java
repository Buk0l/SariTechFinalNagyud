package com.example.saritechnew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Products> productsList = new ArrayList<>();

    public void setProducts(List<Products> productList) {
        productsList.clear();
        productsList.addAll(productList);
        notifyDataSetChanged();
    }

    public void updateProduct(Products product, int position) {
        productsList.set(position, product);
        notifyItemChanged(position);
    }

    public void addProduct(Products product) {
        productsList.add(product);
        notifyItemInserted(productsList.size() - 1);
    }

    public void removeProduct(int position) {
        if (position >= 0 && position < productsList.size()) {
            productsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        // Bind more product details to the corresponding views
    }

    @Override
    public int getItemCount() {
        if (productsList == null || productsList.isEmpty()) {
            return 0;
        } else {
            return productsList.size();
        }
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPrice;
        // Declare more views for other product details

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            // Initialize other views for other product details
        }
    }
}
