package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Receipt extends AppCompatActivity {

    private TextView TransactionTypeTV, TransactionTypeInsideTV, utilityAccIDTV, utilityAccNameTV, accEmailTV, utilityDueDateTV, amountTV, ReceiptRef, timeStamp, dateStamp;
    private LinearLayout paymentNumber, paymentName, paymentDue;  // Initialize LinearLayouts for payment-related fields
    private Button btnExit;
    String TransactionType, transactionType, utilityAccID, utilityAccName, utilityDueDate, amount;

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

        TransactionTypeTV = findViewById(R.id.TransactionTypeTV);
        TransactionTypeInsideTV = findViewById(R.id.TransactionTypeInsideTV);
        utilityAccIDTV = findViewById(R.id.utilityAccIDTV);
        utilityAccNameTV = findViewById(R.id.utilityAccNameTV);
        accEmailTV = findViewById(R.id.accEmailTV);
        utilityDueDateTV = findViewById(R.id.utilityDueDateTV);
        amountTV = findViewById(R.id.amountTV);
        ReceiptRef = findViewById(R.id.ReceiptRef);
        timeStamp = findViewById(R.id.timeStamp);
        dateStamp = findViewById(R.id.dateStamp);

        paymentNumber = findViewById(R.id.paymentNumber);
        paymentName = findViewById(R.id.paymentName);
        paymentDue = findViewById(R.id.paymentDue);

        dateStamp.setText(DateStamp());
        timeStamp.setText(TimeStamp());

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            Intent i = new Intent(Receipt.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        fetchData();
    }

    private void fetchData() {
        Intent intent = getIntent();
        transactionType = intent.getStringExtra("transactionType");
        TransactionType = intent.getStringExtra("TransactionType");
        utilityAccID = intent.getStringExtra("utilityAccID");
        utilityAccName = intent.getStringExtra("utilityAccName");
        utilityDueDate = intent.getStringExtra("utilityDueDate");
        amount = intent.getStringExtra("dueCardAmount");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);

            userRef.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String accEmail = snapshot.getValue(String.class);

                    TransactionTypeTV.setText(TransactionType + " Receipt");
                    TransactionTypeInsideTV.setText(TransactionType);
                    accEmailTV.setText(accEmail != null ? accEmail : "N/A");
                    utilityAccIDTV.setText(utilityAccID);
                    utilityAccNameTV.setText(utilityAccName);
                    utilityDueDateTV.setText(utilityDueDate);
                    amountTV.setText(amount);

                    if ("Payment".equals(transactionType)) {
                        paymentNumber.setVisibility(View.VISIBLE);
                        paymentName.setVisibility(View.VISIBLE);
                        paymentDue.setVisibility(View.VISIBLE);
                    } else {
                        paymentNumber.setVisibility(View.GONE);
                        paymentName.setVisibility(View.GONE);
                        paymentDue.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Receipt", "Failed to fetch email: " + error.getMessage());
                    accEmailTV.setText("Email not found");
                }
            });
        } else {
            accEmailTV.setText("User not logged in");
        }
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
