package com.example.saritechnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.products.ProductDatabase;
import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Products> productsList = new ArrayList<>();
    ProductDatabase dbHelper;

    private final Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        dbHelper = new ProductDatabase(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(List<Products> productList) {
        productsList.clear();
        productsList.addAll(productList);
        notifyDataSetChanged();
    }

    public void updateProduct(Products product, int position) {
        int productId = product.getId();
        String productName = product.getName();
        double productPrice = product.getPrice();
        String productBarcode = String.valueOf(product.getBarcode()); // Convert int to String
        int productQuantity = product.getQuantity();
        String productPhotoPath = product.getPhotoPath();

        Products updatedProduct = new Products(productId, productName, productPrice, productBarcode, productQuantity, productPhotoPath);
        productsList.set(position, updatedProduct);
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
                    showEditProductDialog(product, position);
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    // Handle delete action
                    showDeleteConfirmationDialog(holder);
                    return true;
                } else {
                    return false;
                }
            });
            // Show the popup menu
            popupMenu.show();
        });
    }

    private void showEditProductDialog(Products product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_product, null);
        builder.setView(view);

        EditText editTextName = view.findViewById(R.id.edit_text_name);
        EditText editTextPrice = view.findViewById(R.id.edit_text_price);
        EditText editTextQuantity = view.findViewById(R.id.edit_text_quantity);

        // Set the initial values of the EditText fields
        editTextName.setText(product.getName());
        editTextPrice.setText(String.valueOf(product.getPrice()));
        editTextQuantity.setText(String.valueOf(product.getQuantity()));

        builder.setPositiveButton("Update", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            String priceText = editTextPrice.getText().toString().trim();
            String quantityText = editTextQuantity.getText().toString().trim();

            // Check if any of the fields are empty
            if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            // Create a new Products object with updated values
            Products updatedProduct = new Products(product.getId(), name, price, null, quantity, null);

            // Update the product in the database
            dbHelper.updateProduct(updatedProduct);

            // Update the product in the adapter
            updateProduct(updatedProduct, position);

            Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(@NonNull ProductViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this item?");

        // Set up the buttons
        builder.setPositiveButton("Delete", (dialog, which) -> {
            int itemPosition = holder.getAbsoluteAdapterPosition();
            Products product1 = productsList.get(itemPosition);
            int productId = product1.getId();
            dbHelper.deleteProduct(productId); // Call the deleteProduct() method from the dbHelper object
            removeProduct(itemPosition);
            Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
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
