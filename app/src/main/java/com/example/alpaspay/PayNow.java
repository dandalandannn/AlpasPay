package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class PayNow extends AppCompatActivity {

    private TextView utilityTypeTV, utilityTypeInsideTV, balanceTV, AccNumTV, AccNameTV, DueDateTV, AmountTV;
    private Button btnConfirmPayment;

    String utilityType, dueCardAmount, dueCardDueDate, balance, utilityAccID, utilityAccName;

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
        utilityTypeTV = findViewById(R.id.utilityTypeTV);
        utilityTypeInsideTV = findViewById(R.id.utilityTypeInsideTV);
        balanceTV = findViewById(R.id.balanceTV);

        DueDateTV = findViewById(R.id.DueDateTV);
        AccNumTV = findViewById(R.id.AccNumTV);
        AccNameTV = findViewById(R.id.AccNameTV);
        AmountTV = findViewById(R.id.AmountTV);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);


        Intent intent = getIntent();
        utilityType = intent.getStringExtra("utilityType");
        dueCardAmount = intent.getStringExtra("dueCardAmount");
        dueCardDueDate = intent.getStringExtra("dueCardDueDate");

        balance = intent.getStringExtra("balance");
        utilityAccID = intent.getStringExtra("utilityAccID");
        utilityAccName = intent.getStringExtra("utilityAccName");

        utilityTypeTV.setText(utilityType);
        utilityTypeInsideTV.setText(utilityType);

        double balanceDouble = Double.parseDouble(balance);
        DecimalFormat formatter = new DecimalFormat("#,###.###");
        String balanceForDisplay = formatter.format(balanceDouble);

        balanceTV.setText(balanceForDisplay);
        AccNumTV.setText(utilityAccID);
        AccNameTV.setText(utilityAccName);
        DueDateTV.setText(dueCardDueDate);
        AmountTV.setText(dueCardAmount);

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConfirmPayment();
            }
        });
    }
    private void handleConfirmPayment() {


        Intent i = new Intent(PayNow.this, Receipt.class);
        i.putExtra("TransactionType", utilityType);
        i.putExtra("utilityAccID", utilityAccID);
        i.putExtra("utilityAccName", utilityAccName);
        i.putExtra("utilityDueDate", dueCardDueDate);
        i.putExtra("amount", dueCardAmount);
        i.putExtra("transactionType", "Payment");

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