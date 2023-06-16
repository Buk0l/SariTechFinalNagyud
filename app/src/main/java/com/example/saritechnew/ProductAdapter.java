package com.example.saritechnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.products.ProductDatabase;
import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Products> productsList = new ArrayList<>();
    ProductDatabase dbHelper;

    public ProductAdapter(Context context) {
        dbHelper = new ProductDatabase(context);
    }

    @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("Price : " + "₱" + product.getPrice());
        holder.textViewQuantity.setText("Quantity : " + product.getQuantity());
        // Bind more product details to the corresponding views

        holder.menuButton.setOnClickListener(view -> {
            // Create a PopupMenu
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.kebab_menu); // Assuming you have a menu resource file named 'product_menu.xml'

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {
                    // Handle edit action
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    // Handle delete action
                    int itemPosition = holder.getAbsoluteAdapterPosition();
                    Products product1 = productsList.get(itemPosition);
                    int productId = product1.getId();
                    dbHelper.deleteProduct(productId); // Call the deleteProduct() method from the dbHelper object
                    removeProduct(itemPosition);
                    return true;
                } else {
                    return false;
                }
            });
            // Show the popup menu
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        if (productsList.isEmpty()) {
            return 0;
        } else {
            return productsList.size();
        }
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPrice;
        public TextView textViewQuantity;
        public ImageView menuButton;
        // Declare more views for other product details

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            menuButton = itemView.findViewById(R.id.menuButton);
            // Initialize other views for other product details
        }
    }
}
