package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PayNow extends AppCompatActivity {

    private TextView utilityNameTextView, utilityNameTextViewInside, remainingBalanceTV, AccNumTV,AccNameTV;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_now);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        utilityNameTextView = findViewById(R.id.utilityNameTextView);
        utilityNameTextViewInside = findViewById(R.id.utilityNameTextViewInside);
        remainingBalanceTV = findViewById(R.id.remainingBalanceTV);
        AccNumTV = findViewById(R.id.AccNumTV);
        AccNameTV = findViewById(R.id.AccNameTV);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        String utilityName = getIntent().getStringExtra("UtilityName");
        String utilityAcc = getIntent().getStringExtra("UtilityAcc");
        String utilityAccID = getIntent().getStringExtra("UtilityAccID");
        String remainingBalance = getIntent().getStringExtra("RemainingBalance");

        utilityNameTextView.setText(utilityName);
        utilityNameTextViewInside.setText(utilityName);
        AccNameTV.setText(utilityAcc);
        AccNumTV.setText(utilityAccID);
        //remainingBalanceTV.setText(remainingBalance);

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmPayment();
            }
        });
    }
    private void handleConfirmPayment() {
        Intent i = new Intent(PayNow.this, Receipt.class);
        startActivity(i);
        finish();
    }
    public void onBackPressed() {
        Intent intent = new Intent(PayNow.this, Home.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}