package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WithrawActivity extends AppCompatActivity {

    String balance;
    TextView balanceTV;
    EditText amountEdit;
    Button btnWithraw;

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_withraw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent i = getIntent();
        balance = i.getStringExtra("balance");
        balanceTV = findViewById(R.id.balance);
        balanceTV.setText("Balance: ₱" + balance);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef = databaseRef.child(uid);

        amountEdit = findViewById(R.id.amountEdit);
        btnWithraw = findViewById(R.id.btnWithraw);

        btnWithraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAmount = amountEdit.getText().toString().trim();
                if (!inputAmount.isEmpty()){
                    int minusAmount = Integer.parseInt(inputAmount);
                    if (minusAmount>0){
                        minusCashToBalance(minusAmount);
                        balanceTV.setText("Balance: ₱" + balance);
                    }
                }
            }
        });

    }
    private void minusCashToBalance(int minusAmount) {
        userRef.child("balance").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String stringBalance = task.getResult().getValue(String.class);
                if (stringBalance != null) {
                    int currentBalance = Integer.parseInt(stringBalance);
                    if (currentBalance<minusAmount){
                        Toast.makeText(WithrawActivity.this, "Not enough balance", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int newBalance = currentBalance - minusAmount;
                    String strNewBalance = String.valueOf(newBalance);

                    userRef.child("balance").setValue(strNewBalance).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            amountEdit.setText("");
                            Toast.makeText(WithrawActivity.this, "Withraw done!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WithrawActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(WithrawActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(WithrawActivity.this, "Failed to fetch!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onBackPressed() {
        Intent i = new Intent(WithrawActivity.this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}