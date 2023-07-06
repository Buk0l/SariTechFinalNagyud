package com.example.saritechnew;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdPageFinal extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
                setContentView(R.layout.thirdpagefinal);

                username = findViewById(R.id.username);
                password = findViewById(R.id.password);
                loginButton = findViewById(R.id.loginButton);

                loginButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if (username.getText().toString().equals("SariTech") && password.getText().toString().equals("1234")){
                            Toast.makeText(ThirdPageFinal.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ThirdPageFinal.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

    }
}
