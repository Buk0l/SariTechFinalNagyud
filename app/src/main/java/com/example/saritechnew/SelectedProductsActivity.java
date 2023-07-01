package com.example.saritechnew;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.products.ProductDatabase;
import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

public class SelectedProductsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Products> selectedProducts = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_products);

        Button checkOut = findViewById(R.id.button_check_out);
        TextView overallPrice = findViewById(R.id.overall);
        EditText moneyEditText = findViewById(R.id.money);
        TextView changeTextView = findViewById(R.id.change);

        checkOut.setOnClickListener(v -> {
            for (Products product : selectedProducts) {
                int selectedQuantity = product.getSelectedQuantity();
                int currentQuantity = product.getQuantity();

                // Subtract the selected quantity from the current quantity
                if(currentQuantity > selectedQuantity) {
                    int newQuantity = currentQuantity - selectedQuantity;
                    if (newQuantity < 0) {
                        newQuantity = 0; // Ensure the quantity doesn't go below zero
                    }
                    // Update the quantity in the database
                    product.setQuantity(newQuantity);
                    Toast.makeText(SelectedProductsActivity.this,"Check out Successful!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SelectedProductsActivity.this, "Check out failed. Please try again.", Toast.LENGTH_SHORT).show();
                }

                try (ProductDatabase productDatabase = new ProductDatabase(this)) {
                    productDatabase.updateProduct(product);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any exceptions that may occur during the database operation
                }
            }

            // Perform any other checkout operations here

            finish();
        });

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
        SelectedProductsAdapter adapter = new SelectedProductsAdapter(selectedProducts, overallPrice, moneyEditText, changeTextView);
        recyclerView.setAdapter(adapter);
        double totalPrice = adapter.getTotalPrice();
        overallPrice.setText("Price: ₱" + totalPrice);

    }


    // RecyclerView Adapter
    static class SelectedProductsAdapter extends RecyclerView.Adapter<SelectedProductsAdapter.ProductViewHolder> {
        private final List<Products> productList;
        private final TextView overallPrice;
        private final EditText moneyEditText;
        private final TextView changeTextView;
        private double totalPrice = 0.0;

        public SelectedProductsAdapter(List<Products> productList, TextView overallPrice, EditText moneyEditText, TextView changeTextView) {
            this.productList = productList;
            this.overallPrice = overallPrice;
            this.moneyEditText = moneyEditText;
            this.changeTextView = changeTextView;
            calculatedTotalPrice();
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product, parent, false);
            return new ProductViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Products product = productList.get(position);
            holder.bindProduct(product);

            holder.setQuantityChangeListener(quantity -> {
                product.setSelectedQuantity(quantity);
                calculatedTotalPrice();
                double totalPrice = getTotalPrice();
                overallPrice.setText("Total Price: ₱" + totalPrice); // Update the displayed total price

                moneyEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not used
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not used
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String moneyText = s.toString().trim();

                        if (!moneyText.isEmpty()) {
                            int money = Integer.parseInt(moneyText);
                            double change = money - totalPrice;

                            changeTextView.setText("Change: ₱" + change); // Display the calculated change
                        } else {
                            changeTextView.setText("Change: ₱0.0"); // Reset the change text if the money text is empty
                        }
                    }
                });
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        private void calculatedTotalPrice() {
            totalPrice = 0.0;

            for (Products products : productList) {
                double price = products.getPrice();
                int quantity = products.getSelectedQuantity();

                if (quantity < 1) {
                    quantity = 1;
                    products.setSelectedQuantity(quantity);
                }

                totalPrice += price * quantity;
            }
        }

        // ViewHolder class
        static class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;
            TextView priceTextView;
            TextView quantityTextView;
            Button minusButton;
            Button addButton;
            EditText quantityEditText;
            private int maxQuantity;
            private QuantityChangeListener quantityChangeListener;

            ProductViewHolder(@NonNull View itemView) {
                super(itemView);

                nameTextView = itemView.findViewById(R.id.text_view_name);
                priceTextView = itemView.findViewById(R.id.text_view_price);
                quantityTextView = itemView.findViewById(R.id.text_view_quantity);
                minusButton = itemView.findViewById(R.id.button_minus);
                addButton = itemView.findViewById(R.id.button_add);
                quantityEditText = itemView.findViewById(R.id.edit_text_quantity);

                minusButton.setOnClickListener(v -> decreaseQuantity());

                addButton.setOnClickListener(v -> increaseQuantity());

                // Add text change listener to quantityEditText
                quantityEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not used
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not used
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        updateQuantity();
                    }
                });
            }

            void setQuantityChangeListener(QuantityChangeListener listener) {
                this.quantityChangeListener = listener;
            }

            interface QuantityChangeListener {
                void onQuantityChanged(int quantity);
            }

            @SuppressLint("SetTextI18n")
            public void bindProduct(Products product) {
                this.maxQuantity = product.getQuantity();

                nameTextView.setText(product.getName());
                priceTextView.setText("Price: ₱" + product.getPrice());
                quantityTextView.setText("Quantity: " + product.getQuantity());

                // Set the initial quantity in the EditText
                quantityEditText.setText(String.valueOf(product.getSelectedQuantity()));

                quantityEditText.setText("1");
                updateQuantity();
            }

            private void decreaseQuantity() {
                // Retrieve the current value from the EditText
                String quantityString = quantityEditText.getText().toString();
                int quantity = Integer.parseInt(quantityString);

                // Perform any desired operations with the value
                if (quantity > 1) {
                    quantity--;
                    quantityEditText.setText(String.valueOf(quantity));
                    updateQuantity();
                }
            }

            private void increaseQuantity() {
                // Retrieve the current value from the EditText
                String quantityString = quantityEditText.getText().toString();
                int quantity = Integer.parseInt(quantityString);

                // Perform any desired operations with the value
                if (quantity < maxQuantity) {
                    quantity++;
                    quantityEditText.setText(String.valueOf(quantity));
                    updateQuantity();
                }
            }

            private void updateQuantity() {
                // Retrieve the current value from the EditText
                String quantityString = quantityEditText.getText().toString();
                int quantity = Integer.parseInt(quantityString);

                // Trigger the quantity change listener
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(quantity);
                }
            }
        }
    }
}
