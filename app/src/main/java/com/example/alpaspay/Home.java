package com.example.alpaspay;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseUser user;
    String userID;
    DatabaseReference ref;
    private FrameLayout logOutCardView;
    private TextView btnLogoutYes,btnLogOutNo;
    private TextView balanceTextView;

    private TextView btnCashIn;
    private LinearLayout btnTransactions;
    private LinearLayout btnWithdraw;
    private LinearLayout btnAccountSettings;
    private LinearLayout btnHelpdesk;
    private double balance;
    private String balanceForDisplay;

    ArrayList<DueCardObj> dueCardObjs = new ArrayList<>();
    private ListView dueList;
    private DueCardListAdapter dueCardListAdapter;


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
        logOutCardView = findViewById(R.id.logOutCardView);
        btnLogOutNo = findViewById(R.id.btnLogOutNo);
        btnLogoutYes = findViewById(R.id.btnLogoutYes);

        balanceTextView = findViewById(R.id.balance);
        btnCashIn = findViewById(R.id.btn_cashin);
        btnTransactions = findViewById(R.id.btn_transactions);
        btnWithdraw = findViewById(R.id.btn_withraw);
        btnAccountSettings = findViewById(R.id.btn_account_settings);
        btnHelpdesk = findViewById(R.id.btn_helpdesk);

        dueList = findViewById(R.id.dueList);

        fetchData();

        btnCashIn.setOnClickListener(v -> handleCashIn());
        btnTransactions.setOnClickListener(v -> handleTransaction());
        btnWithdraw.setOnClickListener(v -> handleWithraw());
        btnAccountSettings.setOnClickListener(v -> handleAccSettings());
        btnHelpdesk.setOnClickListener(v -> handleHelpDesk());

        btnLogoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });
        btnLogOutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCardView.setVisibility(View.GONE);
            }
        });
    }

    private void fetchData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String balanceStr = snapshot.child("balance").getValue(String.class);
                    try {
                        balance = balanceStr != null ? Double.parseDouble(balanceStr) : 0.0;
                    } catch (NumberFormatException e) {
                        balance = 0.0;
                    }

                    DecimalFormat formatter = new DecimalFormat("#,###.###");
                    balanceForDisplay = formatter.format(balance);
                    balanceTextView.setText(balanceForDisplay);

                    int numberOfChildren = (int) snapshot.child("Utilities").getChildrenCount();
                    Toast.makeText(getApplicationContext(), "Number of children" + numberOfChildren, Toast.LENGTH_SHORT).show();
                    dueCardObjs.clear();

                    for (int i = 1; i <= 3; i++) {
                        String utilityType = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityType").getValue(String.class);
                        String utilityAccName = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityAccName").getValue(String.class);
                        String utilityAccID = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityAccID").getValue(String.class);
                        String dueCardAmount = snapshot.child("Utilities").child(String.valueOf(i)).child("dueCardAmount").getValue(String.class);
                        String dueCardDueDate = snapshot.child("Utilities").child(String.valueOf(i)).child("dueDate").getValue(String.class);

                        if (utilityType != null) {
                            DueCardObj dueCardObj = new DueCardObj(utilityType, dueCardAmount, dueCardDueDate, balanceForDisplay, utilityAccID, utilityAccName);
                            dueCardObjs.add(dueCardObj);
                        }
                        dueCardListAdapter = new DueCardListAdapter(Home.this, dueCardObjs);
                        dueList.setAdapter(dueCardListAdapter);

                        ListAdapter listAdapter = new ListAdapter() {
                            @Override
                            public boolean areAllItemsEnabled() {
                                return false;
                            }

                            @Override
                            public boolean isEnabled(int position) {
                                return false;
                            }

                            @Override
                            public void registerDataSetObserver(DataSetObserver observer) {

                            }

                            @Override
                            public void unregisterDataSetObserver(DataSetObserver observer) {

                            }

                            @Override
                            public int getCount() {
                                return 0;
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public boolean hasStableIds() {
                                return false;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                return null;
                            }

                            @Override
                            public int getItemViewType(int position) {
                                return 0;
                            }

                            @Override
                            public int getViewTypeCount() {
                                return 0;
                            }

                            @Override
                            public boolean isEmpty() {
                                return false;
                            }

                        };
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("HomeActivity", "Data fetch failed: " + error.getMessage());
                }
            });
        }
    }

    private void handleCashIn() {
        startActivity(new Intent(Home.this, CashIn.class));
        finish();
    }

    private void handleTransaction() {
        startActivity(new Intent(Home.this, TransactionHistory.class));
    }

    private void handleWithraw() {
        Intent i = new Intent(Home.this, WithrawActivity.class);
        i.putExtra("balance", balanceForDisplay);
        startActivity(i);
    }

    private void handleAccSettings() {
        startActivity(new Intent(Home.this, AccountSettings.class));
    }

    private void handleHelpDesk() {
        startActivity(new Intent(Home.this, HelpDesk.class));
    }

    public void onBackPressed() {
        logOutCardView.setVisibility(View.VISIBLE);
    }

    protected void onResume() {
        super.onResume();
        fetchData();
    }
}
