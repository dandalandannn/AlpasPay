package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    private TextView balanceTextView, utility1, utility2, utility3;
    private TextView btnCashIn;
    private LinearLayout btnTransactions;
    private LinearLayout btnWithdraw;
    private LinearLayout btnAccountSettings;
    private LinearLayout btnHelpdesk;
    private Button btnPayNow1, btnPayNow2, btnPayNow3;

    private String balance;
    private String utilityAcc1, utilityAcc2, utilityAcc3;
    private String utilityAccID1, utilityAccID2, utilityAccID3;

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

        btnPayNow1 = findViewById(R.id.btnDueCardPayNow1);
        btnPayNow2 = findViewById(R.id.btnDueCardPayNow2);
        btnPayNow3 = findViewById(R.id.btnDueCardPayNow3);
        utility1 = findViewById(R.id.utility1);
        balanceTextView = findViewById(R.id.balance);
        btnCashIn = findViewById(R.id.btn_cashin);
        btnTransactions = findViewById(R.id.btn_transactions);
        btnWithdraw = findViewById(R.id.btn_withraw);
        btnAccountSettings = findViewById(R.id.btn_account_settings);
        btnHelpdesk = findViewById(R.id.btn_helpdesk);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String username = snapshot.child("username").getValue(String.class);
                    String utilityUna = snapshot.child("Utilities").child("1").child("utilityType").getValue(String.class);
                    utility1.setText(utilityUna);
                    String utilityTwo = snapshot.child("Utilities").child("2").child("utilityType").getValue(String.class);
                    utility2.setText(utilityTwo);
                    String utilityThree = snapshot.child("Utilities").child("3").child("utilityType").getValue(String.class);
                    utility3.setText(utilityThree);

                    utilityAcc1 = snapshot.child("Utilities").child("1").child("utilityAccName").getValue(String.class);
                    utilityAcc2 = snapshot.child("Utilities").child("2").child("utilityAccName").getValue(String.class);
                    utilityAcc3 = snapshot.child("Utilities").child("3").child("utilityAccName").getValue(String.class);

                    utilityAccID1 = snapshot.child("Utilities").child("1").child("utilityAccID").getValue(String.class);
                    utilityAccID2 = snapshot.child("Utilities").child("2").child("utilityAccID").getValue(String.class);
                    utilityAccID3 = snapshot.child("Utilities").child("3").child("utilityAccID").getValue(String.class);

                    balance = snapshot.child("balance").getValue(String.class);
                    if (balance == null || balance.trim().isEmpty()) {
                        balance = "0";
                    }
                    balanceTextView.setText(balance);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomeActivity", "Data fetch failed: " + error.getMessage());
                }
            });
        }
        btnPayNow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, PayNow.class);
                i.putExtra("UtilityName", utility1.getText().toString());
                i.putExtra("UtilityAcc", utilityAcc1);
                i.putExtra("UtilityAccID", utilityAccID1);
                startActivity(i);
                finish();
            }
        });
        btnPayNow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, PayNow.class);
                i.putExtra("UtilityName", utility2.getText().toString());
                i.putExtra("UtilityAcc", utilityAcc2);
                i.putExtra("UtilityAccID", utilityAccID2);
                startActivity(i);
                finish();
            }
        });
        btnPayNow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, PayNow.class);
                i.putExtra("UtilityName", utility3.getText().toString());
                i.putExtra("UtilityAcc", utilityAcc3);
                i.putExtra("UtilityAccID", utilityAccID3);
                startActivity(i);
                finish();
            }
        });

        btnCashIn.setOnClickListener(v -> handleCashIn());
        btnTransactions.setOnClickListener(v -> handleTransaction());
        btnWithdraw.setOnClickListener(v -> handleWithraw());
        btnAccountSettings.setOnClickListener(v -> handleAccSettings());
        btnHelpdesk.setOnClickListener(v -> handleHelpDesk());
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
