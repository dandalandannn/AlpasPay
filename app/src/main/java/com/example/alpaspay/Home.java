package com.example.alpaspay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView btnCashIn;
    private LinearLayout btnTransactions;
    private LinearLayout btnWithdraw;
    private LinearLayout btnAccountSettings;
    private LinearLayout btnHelpdesk;

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