//package com.example.saritechnew.products;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.saritechnew.Activity.ProductAdapter;
//import com.example.saritechnew.R;
//
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import com.example.saritechnew.products.ProductDatabase;
//import android.content.Context;
//
//import java.util.List;
//
//public class InventoryList extends AppCompatActivity {
//
//    private ProductAdapter productAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.show_inventory_list);
//
//        // Initialize the RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize the ProductAdapter
//        productAdapter = new ProductAdapter();
//        recyclerView.setAdapter(productAdapter);
//
//        // Retrieve the product list from the database
//        ProductDatabase productDatabase = new ProductDatabase(this);
//        List<Products> productList = productDatabase.getListProducts();
//
//        // Set the product list to the adapter
//        productAdapter.setProducts(productList);
//    }
//}
