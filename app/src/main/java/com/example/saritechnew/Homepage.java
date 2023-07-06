package com.example.saritechnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {

    Button homepageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        homepageButton = findViewById(R.id.homepageButton);

        homepageButton.setOnClickListener(this::myButtonClick);
    }

    public void myButtonClick(View view){
        Intent intent = new Intent(this, LoginSignup.class);
        startActivity(intent);
    }
}
