package com.example.saritechnew.BarcodeScanners;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.saritechnew.PermissionUtils;
import com.example.saritechnew.R;
import com.example.saritechnew.products.ProductDatabase;
import com.example.saritechnew.products.Products;

import java.util.List;

public class AddOnlyBarcodeScanner extends AppCompatActivity {
        private CodeScanner mCodeScanner;
        private String scannedBarcode;

        private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_barcode_scanner);
            CodeScannerView scannerView = findViewById(R.id.scanner_view_add);
            mCodeScanner = new CodeScanner(this, scannerView);

            // Set the decode callback for the code scanner
            mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
                scannedBarcode = result.getText();
                Toast.makeText(AddOnlyBarcodeScanner.this, result.getText(), Toast.LENGTH_SHORT).show();

                // Check if the barcode is recognized
                if (isBarcodeRecognized(scannedBarcode)) {
                    // Barcode is recognized, show the product details
                    showProductDetails(scannedBarcode);
                } else {
                    // Barcode is not recognized, ask to add the product
                    showInsertProductDialog(scannedBarcode);
                }

            }));

            scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

            if (!PermissionUtils.checkCameraPermission(this)) {
                PermissionUtils.requestCameraPermission(this, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                if (PermissionUtils.handlePermissionsResult(grantResults)) {
                    mCodeScanner.startPreview();
                }
            }
        }

        private boolean isBarcodeRecognized(String barcode) {
            Log.d("BarcodeScanner", "Scanned Barcode: " + barcode);

            try (ProductDatabase productDatabase = new ProductDatabase(this)) {
                // Query the database to get products with the matching barcode
                List<Products> productList = productDatabase.getAllProducts(barcode);

                Log.d("BarcodeScanner", "Product List Size: " + productList.size());

                // Check if the productList is not empty, indicating a matching barcode
                return !productList.isEmpty();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @SuppressLint("SetTextI18n")
        private void showProductDetails(String barcode) {
            try (ProductDatabase productDatabase = new ProductDatabase(this)) {
                // Query the database to get the product details
                List<Products> productList = productDatabase.getAllProducts(barcode);

                Log.d("BarcodeScanner", "Product List Size: " + productList.size());

                if (!productList.isEmpty()) {
                    // Create the dialog using the dialog_product_details layout
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_product_details, null);
                    builder.setView(dialogView);

                    // Find the TextViews in the dialog layout
                    TextView nameTextView = dialogView.findViewById(R.id.text_view_name);
                    TextView priceTextView = dialogView.findViewById(R.id.text_view_price);
                    TextView quantityTextView = dialogView.findViewById(R.id.text_view_quantity);

                    // Set the product details in the TextViews
                    StringBuilder names = new StringBuilder();
                    StringBuilder prices = new StringBuilder();
                    StringBuilder quantities = new StringBuilder();
                    for (Products product : productList) {
                        names.append(product.getName()).append("\n");
                        prices.append(product.getPrice()).append("\n");
                        quantities.append(product.getQuantity()).append("\n");
                    }
                    nameTextView.setText("Name:\n" + names);
                    priceTextView.setText("Price:\n" + prices);
                    quantityTextView.setText("Quantity:\n" + quantities);

                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    // Product details not found for the barcode
                    Toast.makeText(this, "Product details not available", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private void showInsertProductDialog(String barcode) {
            // Create an instance of the AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_insert_product, null);

            // Find the input fields and buttons in the dialog layout
            Button positiveButton = dialogView.findViewById(R.id.button_insert);
            Button negativeButton = dialogView.findViewById(R.id.button_cancel);
            EditText nameEditText = dialogView.findViewById(R.id.edit_text_name);
            EditText priceEditText = dialogView.findViewById(R.id.edit_text_price);
            EditText quantityEditText = dialogView.findViewById(R.id.edit_text_quantity);

            positiveButton.setText("Add");
            negativeButton.setText("Cancel");

            AlertDialog alertDialog = builder.setView(dialogView).show(); // Show the dialog and obtain the AlertDialog reference

            positiveButton.setOnClickListener(v -> {
                // Get the product information from the input fields
                String name = nameEditText.getText().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                int quantity = Integer.parseInt(quantityEditText.getText().toString());

                // Insert the product into the database
                try (ProductDatabase productDatabase = new ProductDatabase(AddOnlyBarcodeScanner.this)) {
                    productDatabase.insertProduct(name, price, barcode, quantity, null);


                }

                Toast.makeText(AddOnlyBarcodeScanner.this, "Product inserted", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss(); // Close the dialog
            });

            negativeButton.setOnClickListener(v -> {
                alertDialog.dismiss(); // Close the dialog
                Toast.makeText(AddOnlyBarcodeScanner.this, "Product not inserted", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            mCodeScanner.startPreview();
        }

        @Override
        protected void onPause() {
            mCodeScanner.releaseResources();
            super.onPause();
        }
    }