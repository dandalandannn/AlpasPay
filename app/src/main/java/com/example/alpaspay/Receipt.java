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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Receipt extends AppCompatActivity {

    private TextView utilityNameTextView, utilityNameTextViewInside, AccNumTV, AccNameTV, DueDateTV, AmountTV;
    private Button btnExit;

    private String utilityType, dueCardAmount, dueCardDueDate, balance, utilityAccID, utilityAccName;
    private TextView dateStamp, timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        utilityType = intent.getStringExtra("utilityType");
        dueCardAmount = intent.getStringExtra("dueCardAmount");
        dueCardDueDate = intent.getStringExtra("dueCardDueDate");

        balance = intent.getStringExtra("balance");
        utilityAccID = intent.getStringExtra("utilityAccID");
        utilityAccName = intent.getStringExtra("utilityAccName");

        utilityNameTextView = findViewById(R.id.utilityNameTextView);
        utilityNameTextViewInside = findViewById(R.id.utilityNameTextViewInside);

        DueDateTV = findViewById(R.id.DueDateTV);
        AccNumTV = findViewById(R.id.AccNumTV);
        AccNameTV = findViewById(R.id.AccNameTV);
        AmountTV = findViewById(R.id.AmountTV);

        dateStamp = findViewById(R.id.dateStamp);
        timeStamp = findViewById(R.id.timeStamp);

        utilityNameTextView.setText(utilityType + " Receipt");
        utilityNameTextViewInside.setText(utilityType);
        AccNumTV.setText(utilityAccID);
        AccNameTV.setText(utilityAccName);
        DueDateTV.setText(dueCardDueDate);
        AmountTV.setText(dueCardAmount);

        dateStamp.setText(DateStamp());
        timeStamp.setText(TimeStamp());

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Receipt.this, Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }

    private String TimeStamp() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return currentDate.format(formatter);
    }

    private String DateStamp() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return currentDate.format(formatter);
    }



    public void onBackPressed() {
        Intent intent = new Intent(Receipt.this, Home.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}