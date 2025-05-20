package com.example.alpaspay;

import android.content.Intent;
import
        android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CashIn extends AppCompatActivity {
    private ImageView qr;
    private LinearLayout AmountLayout;
    private TextView emailTV,AmountTV;
    private EditText amountEdit;
    private double amount;
    private boolean naH;

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String uid;
    private double currentBalance;
    private String stringBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cash_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        qr = findViewById(R.id.qr);
        AmountLayout = findViewById(R.id.AmountLayout);
        emailTV = findViewById(R.id.emailTV);
        AmountTV = findViewById(R.id.AmountTV);
        amountEdit = findViewById(R.id.amountEdit);

        naH = false;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef = databaseRef.child(uid);
        fetchData();

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(CashIn.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (naH){
                    addCashToBalance(amount);
                }

            }
        });
        displayInput();
    }

    private void fetchData() {
        userRef.child("balance").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                stringBalance = task.getResult().getValue(String.class);
            } else {
                Toast.makeText(CashIn.this, "Failed to fetch!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInput() {
        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();

                if (!input.isEmpty()) {
                    try {
                        amount = Double.parseDouble(input);

                        Log.d("tangina", "amount: " + amount);

                        if (amount > 100000) {
                            AmountLayout.setVisibility(View.VISIBLE);
                            AmountTV.setText("Invalid amount!");
                            naH = false;

                        } else if (amount > 0) {
                            DecimalFormat formatter = new DecimalFormat("#,###.###");
                            String formattedAmount = formatter.format(amount);

                            AmountTV.setText(formattedAmount);
                            AmountLayout.setVisibility(View.VISIBLE);
                            naH = true;

                        } else {
                            AmountLayout.setVisibility(View.INVISIBLE);
                            AmountTV.setText("₱ 0");
                            naH = false;
                        }

                    } catch (NumberFormatException e) {
                        AmountLayout.setVisibility(View.INVISIBLE);
                        AmountTV.setText("0");
                        naH = false;
                    }

                } else {
                    AmountLayout.setVisibility(View.INVISIBLE);
                    AmountTV.setText("0");
                    naH = false;
                }
            }
        });
    }

    private void addCashToBalance(double addedAmount) {
        if (stringBalance != null) {
            currentBalance = Double.parseDouble(stringBalance);
            double newBalance = currentBalance + addedAmount;
            String strNewBalance = String.valueOf(newBalance);

            userRef.child("balance").setValue(strNewBalance).addOnCompleteListener(updateTask -> {
                if (updateTask.isSuccessful()) {
                    amountEdit.setText("");
                    Toast.makeText(CashIn.this, "Cash in done!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CashIn.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CashIn.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed() {
        Intent i = new Intent(CashIn.this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }}