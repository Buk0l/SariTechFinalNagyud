package com.example.saritechnew;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

public class SelectedProductsActivity extends AppCompatActivity {

    private List<Products> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedProducts = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_products);

        ArrayList<Parcelable> parcelables = getIntent().getParcelableArrayListExtra("selectedProducts");
        if (parcelables != null) {
            for (Parcelable parcelable : parcelables) {
                if (parcelable instanceof Products) {
                    selectedProducts.add((Products) parcelable);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SelectedProductsAdapter adapter = new SelectedProductsAdapter(selectedProducts);
        recyclerView.setAdapter(adapter);
    }

    // Custom RecyclerView Adapter
    class SelectedProductsAdapter extends RecyclerView.Adapter<SelectedProductsAdapter.ProductViewHolder> {
        private final List<Products> productList;

        public SelectedProductsAdapter(List<Products> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Products product = productList.get(position);

            holder.nameTextView.setText(product.getName());
            holder.priceTextView.setText(String.valueOf(product.getPrice()));
            holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        // ViewHolder class
        class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView priceTextView;
            TextView quantityTextView;

            ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.text_view_name);
                priceTextView = itemView.findViewById(R.id.text_view_price);
                quantityTextView = itemView.findViewById(R.id.text_view_quantity);
            }
        }

        private void showOverallListDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectedProductsActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.show_cart_list, null);
            builder.setView(dialogView);

            TextView overallListTextView = dialogView.findViewById(R.id.show_cart_list);

            // Create a StringBuilder to accumulate the overall list text
            StringBuilder overallListText = new StringBuilder();
            for (Products product : selectedProducts) {
                overallListText.append("Name: ").append(product.getName()).append("\n")
                        .append("Price: ").append(product.getPrice()).append("\n")
                        .append("Quantity: ").append(product.getQuantity()).append("\n\n");
            }

            overallListTextView.setText(overallListText.toString());

            AlertDialog alertDialog = builder.create();
            builder.setPositiveButton("OK", null);
            alertDialog.show();
        }
    }
}

