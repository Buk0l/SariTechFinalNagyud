package com.example.saritechnew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saritechnew.BarcodeScanners.AddOnlyBarcodeScanner;
import com.example.saritechnew.Activity.ProductAdapter;
import com.example.saritechnew.R;
import com.example.saritechnew.products.ProductDatabase;
import com.example.saritechnew.products.Products;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class InventoryFragment extends Fragment {

    private ProductAdapter productAdapter;
    private List<Products> productList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ImageView addButton = view.findViewById(R.id.addButton);
        SearchView searchView = view.findViewById(R.id.searchView);

        addButton.setOnClickListener(v -> {
            // Perform the action when the addButton is clicked
            Intent intent = new Intent(requireContext(), AddOnlyBarcodeScanner.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation here
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search filtering here
                filterSearch(newText);
                return true;
            }
        });

        // Initialize the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the ProductAdapter
        productAdapter = new ProductAdapter(requireContext());
        recyclerView.setAdapter(productAdapter);

        // Retrieve the product list from the database using try-with-resources
        try (ProductDatabase productDatabase = new ProductDatabase(getActivity())) {
            productList = productDatabase.getListProducts();
            // Set the product list to the adapter
            productAdapter.setProducts(productList);
        }
        return view;
    }

    private void performSearch(String query) {
        List<Products> searchResults = new ArrayList<>();

        // Perform search operation using the query
        for (Products product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(product);
            }
        }

        // Update the adapter dataset with search results
        productAdapter.setProducts(searchResults);
    }

    private void filterSearch(String newText) {
        List<Products> filteredResults = new ArrayList<>();

        // Filter the dataset based on the new text
        for (Products product : productList) {
            if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredResults.add(product);
            }
        }

        // Update the adapter dataset with filtered results
        productAdapter.setProducts(filteredResults);
    }
}