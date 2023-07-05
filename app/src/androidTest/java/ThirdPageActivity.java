import static android.os.Build.VERSION_CODES.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ThirdPageActivity extends AppCompatActivity {

    private ImageButton imgArrow;
    private ImageButton imgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdpage);

        imgArrow = findViewById(R.id.img_arrow);
        imgLogin = findViewById(R.id.img_login);

        imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle arrow image button click
                onLayoutClicked();
            }
        });

        imgLgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login image button click
                // Implement your logic here
            }
        });
    }

    public void onLayoutClicked() {
        // Handle layout click
        // Implement your logic here
    }
}
