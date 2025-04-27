package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountSettings extends AppCompatActivity {
    private FrameLayout logOutCardView;
    private TextView btnLogoutYes,btnLogOutNo,btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        logOutCardView = findViewById(R.id.logOutCardView);
        btnLogOutNo = findViewById(R.id.btnLogOutNo);
        btnLogoutYes = findViewById(R.id.btnLogoutYes);
        btnLogOut = findViewById(R.id.btnLogOut);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCardView.setVisibility(View.VISIBLE);
            }
        });
        btnLogoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountSettings.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        btnLogOutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCardView.setVisibility(View.GONE);
            }
        });
    }

}