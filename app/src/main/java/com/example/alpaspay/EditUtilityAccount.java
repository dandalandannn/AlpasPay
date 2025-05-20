package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditUtilityAccount extends AppCompatActivity {
    FirebaseUser user;
    String userID;
    DatabaseReference ref;
    TextView utilityTypeTV, accNameTV, utilityIDTV, utilityBillerTV, utilityStatusTV, dueAmountTV;
    Button btnPayNow, btnTransactions;
    TextView btnDeleteUtility;
    private String utilityTypeID;

    String utilityType, utilityAccName, utilityAccID, dueAmount, dueDate, status;
    double balance = 0.0;
    String balanceForDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_utility_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        utilityTypeTV = findViewById(R.id.utilityType);
        accNameTV = findViewById(R.id.accName);
        utilityIDTV = findViewById(R.id.utilityID);
        utilityBillerTV = findViewById(R.id.utilityBiller);
        utilityStatusTV = findViewById(R.id.utilityStatus);
        dueAmountTV = findViewById(R.id.dueAmount);

        btnPayNow = findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(v -> handlePayNow());

        btnTransactions = findViewById(R.id.btnTransactions);
        btnTransactions.setOnClickListener(v -> handleTransactions());

        btnDeleteUtility = findViewById(R.id.btnDeleteUtility);
        btnDeleteUtility.setOnClickListener(v -> handleDeleteUtility());

        utilityTypeID = getIntent().getStringExtra("utilityTypeID");
        fetchData();
    }

    private void fetchData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    utilityType = snapshot.child("Utilities").child(utilityTypeID).child("utilityType").getValue(String.class);
                    utilityAccName = snapshot.child("Utilities").child(utilityTypeID).child("utilityAccName").getValue(String.class);
                    utilityAccID = snapshot.child("Utilities").child(utilityTypeID).child("utilityAccID").getValue(String.class);
                    dueAmount = snapshot.child("Utilities").child(utilityTypeID).child("dueCardAmount").getValue(String.class);
                    dueDate = snapshot.child("Utilities").child(utilityTypeID).child("dueDate").getValue(String.class);
                    status = snapshot.child("status").getValue(String.class);

                    String balanceStr = snapshot.child("balance").getValue(String.class);
                    try {
                        balance = balanceStr != null ? Double.parseDouble(balanceStr) : 0.0;
                    } catch (NumberFormatException e) {
                        balance = 0.0;
                    }
                    balanceForDisplay = String.valueOf(balance);

                    Log.d("FirebaseData", "UtilityType: " + utilityType);
                    Log.d("FirebaseData", "DueAmount: " + dueAmount);
                    Log.d("FirebaseData", "DueDate: " + dueDate);
                    Log.d("FirebaseData", "Balance: " + balanceForDisplay);
                    Log.d("FirebaseData", "UtilityAccID: " + utilityAccID);
                    Log.d("FirebaseData", "UtilityAccName: " + utilityAccName);

                    updateViews();
                } else {
                    Toast.makeText(EditUtilityAccount.this, "Utility not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EditUtility", "Fetch failed: " + error.getMessage());
            }
        });
    }
    }

    private void updateViews() {
        utilityTypeTV.setText(utilityType + " Account");
        accNameTV.setText(utilityAccName != null ? utilityAccName : "N/A");
        utilityIDTV.setText("ID: " + (utilityAccID != null ? utilityAccID : "N/A"));
        utilityBillerTV.setText("Biller: " + utilityType + " Biller");
        dueAmountTV.setText("₱" + (dueAmount != null ? dueAmount : "0.00"));
        utilityStatusTV.setText("Status: " + (status != null ? status : "Active"));
    }

    private void handleDeleteUtility() {
        if (user == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        DatabaseReference userUtilityRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid)
                .child("Utilities")
                .child(String.valueOf(utilityTypeID));

        userUtilityRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteData", "Utility deleted successfully");
                    Toast.makeText(getApplicationContext(), "Utility deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteData", "Failed to delete utility: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Failed to delete utility", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleTransactions() {
        // TODO: Open transaction history
    }

    private void handlePayNow() {
        Intent i = new Intent(EditUtilityAccount.this, PayNow.class);
        i.putExtra("utilityType", utilityType);
        i.putExtra("dueCardAmount", dueAmount);
        i.putExtra("dueCardDueDate", dueDate);
        i.putExtra("balance", balanceForDisplay);
        i.putExtra("utilityAccID", utilityAccID);
        i.putExtra("utilityAccName", utilityAccName);
        startActivity(i);
    }
}
