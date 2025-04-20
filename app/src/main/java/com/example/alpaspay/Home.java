package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class Home extends AppCompatActivity {
    FirebaseUser user;
    String userID;
    DatabaseReference ref;

    private TextView balanceTextView, utilityTextView1;
    private TextView btnCashIn;
    private LinearLayout btnTransactions;
    private LinearLayout btnWithdraw;
    private LinearLayout btnAccountSettings;
    private LinearLayout btnHelpdesk;

    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        utilityTextView1 = findViewById(R.id.utility1);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);

                String utility = snapshot.child("utilities").child("1").child("utilityType").getValue(String.class);
                utilityTextView1.setText(utility);

                balance = snapshot.child("balance").getValue(String.class);
                if (balance != null) {
                    balanceTextView.setText(balance);
                } else {
                    balanceTextView.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        balanceTextView = findViewById(R.id.balance);
        btnCashIn = findViewById(R.id.btn_cashin);
        btnTransactions = findViewById(R.id.btn_transactions);
        btnWithdraw = findViewById(R.id.btn_withraw);
        btnAccountSettings = findViewById(R.id.btn_account_settings);
        btnHelpdesk = findViewById(R.id.btn_helpdesk);

        btnCashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCashIn();
            }
        });

        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTransaction();
            }
        });
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWithraw();
            }
        });
        btnAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAccSettings();
            }
        });
        btnHelpdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleHelpDesk();
            }
        });
    }

    private void handleCashIn() {
        startActivity(new Intent(Home.this, CashIn.class));
    }
    private void handleTransaction() {
        startActivity(new Intent(Home.this, TransactionHistory.class));
    }
    private void handleWithraw() {
        startActivity(new Intent(Home.this, WithrawActivity.class));
    }

    private void handleAccSettings() {
        startActivity(new Intent(Home.this, AccountSettings.class));
    }

    private void handleHelpDesk() {
        startActivity(new Intent(Home.this, HelpDesk.class));
    }
}