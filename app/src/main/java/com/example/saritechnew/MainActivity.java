package com.example.saritechnew;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.saritechnew.databinding.ActivityMainBinding;
import com.example.saritechnew.ui.HistoryFragment;
import com.example.saritechnew.ui.HomeFragment;
import com.example.saritechnew.ui.InventoryFragment;
import com.example.saritechnew.ui.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.saritechnew.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.navigation_history) {
                replaceFragment(new HistoryFragment());
            } else if (itemId == R.id.navigation_inventory) {
                replaceFragment(new InventoryFragment());
            } else if (itemId == R.id.navigation_profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        FloatingActionButton fab = findViewById(R.id.barcode_scanner);
        fab.setOnClickListener(v -> {
            // Perform your desired action when the FAB button is clicked
            // For example, you can start the BarcodeScanner activity
            Intent intent = new Intent(MainActivity.this, BarcodeScanner.class);
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

}

