package com.example.alpaspay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AccountSettings extends AppCompatActivity {

    // New Utility Account
    FrameLayout utilityForm;
    ImageView btnCloseForm;
    TextView btnAddUtility;
    private TextView utilityPrompt, accIdPrompt, namePrompt;
    private RadioGroup rgroup;
    private int selectedID;
    private Button btnToSignup;
    private TextView linkedAccTV;
    private RadioButton selectedRadio;
    private String utility;
    private EditText accIDfield;
    private String accID;
    private EditText firstNameField;
    private EditText lastNameField;
    private String firstName, lastName, accName;
    private Button btn_validID;
    private boolean cont;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Animation shake;

    // fetch data
    boolean kuryente, internet, tubig;
    FirebaseUser user;
    String userID;
    DatabaseReference ref;
    private FrameLayout logOutCardView;
    private TextView btnKuryente, btnInternet, btnTubig;
    private TextView btnLogoutYes, btnLogOutNo, btnLogOut;
    private String kuryenteAccName, kuryenteID;
    private String internetAccName, internetID;
    private String tubigAccName, tubigID;

    RadioButton radio_option1, radio_option2, radio_option3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnKuryente = findViewById(R.id.btnKuryente);
        btnKuryente.setOnClickListener(v -> handleUtility(1));
        btnInternet = findViewById(R.id.btnInternet);
        btnInternet.setOnClickListener(v -> handleUtility(2));
        btnTubig = findViewById(R.id.btnTubig);
        btnTubig.setOnClickListener(v -> handleUtility(3));

        //Logout
        logOutCardView = findViewById(R.id.logOutCardView);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOutNo = findViewById(R.id.btnLogOutNo);
        btnLogOut.setOnClickListener(v -> logOutCardView.setVisibility(View.VISIBLE));
        btnLogOutNo.setOnClickListener(v -> logOutCardView.setVisibility(View.GONE));

        btnLogoutYes = findViewById(R.id.btnLogoutYes);
        if (btnLogoutYes != null) {
            btnLogoutYes.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(AccountSettings.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            });}

        utilityPrompt = findViewById(R.id.utilityPrompt);
        accIdPrompt = findViewById(R.id.accIdPrompt);
        namePrompt = findViewById(R.id.namePrompt);
        rgroup = findViewById(R.id.options);
        btnToSignup = findViewById(R.id.btn_continueToSignUp);
        accIDfield = findViewById(R.id.account_id);
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        cont = false;

        btn_validID = findViewById(R.id.btn_validID);

        utilityForm = findViewById(R.id.utilityForm);
        btnCloseForm = findViewById(R.id.btnCloseForm);

        fetchData();
        handleUtilityForm();

        linkedAccTV = findViewById(R.id.linkedAccTV);
        btnAddUtility = findViewById(R.id.btnAddUtility);
        utilityForm = findViewById(R.id.utilityForm);
        btnCloseForm = findViewById(R.id.btnCloseForm);

        radio_option1 = findViewById(R.id.radio_option1);
        radio_option2 = findViewById(R.id.radio_option2);
        radio_option3 = findViewById(R.id.radio_option3);

        btnAddUtility.setOnClickListener(v -> utilityForm.setVisibility(View.VISIBLE));
        btnCloseForm.setOnClickListener(v -> closeForm());
        btnLogOut.setOnClickListener(v -> logOutCardView.setVisibility(View.VISIBLE));

    }

    private void addUtility() {
        UserUtilityRegistration userUtilityRegistration = new UserUtilityRegistration(
                utility,
                accID,
                accName,
                15
        );

        Log.d("CreateData", "Created UserUtilityRegistration object");
        String uid = user.getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userRef = databaseRef.child(uid);

        String utilityTypeID;

        if(utility.equals("Kuryente")){
            utilityTypeID = "1";
        } else if (utility.equals("Internet")) {
            utilityTypeID = "2";
        } else if (utility.equals("Tubig")) {
            utilityTypeID = "3";
        }else {
            utilityTypeID = "-1";
        }

        userRef.child("Utilities").child(utilityTypeID).setValue(userUtilityRegistration)
                .addOnFailureListener(e -> {
                    Log.e("CreateData", "Failed to save utility data: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Failed to save utility data", Toast.LENGTH_SHORT).show();
                });
        fetchData();
        closeForm();
    }

    private void handleUtilityForm() {
        btn_validID.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnToSignup.setOnClickListener(v -> {
            cont = true;
            selectedID = rgroup.getCheckedRadioButtonId();
            accID = accIDfield.getText().toString().trim();
            firstName = firstNameField.getText().toString().trim();
            lastName = lastNameField.getText().toString().trim();
            accName = firstName + " " + lastName;

            if (accID.isEmpty() || firstName.isBlank() || lastName.isBlank() || selectedID == -1) {
                if (accID.isEmpty()) {
                    shakeAccIDPrompt();
                }
                if (firstName.isBlank() || lastName.isBlank()) {
                    shakeNamePrompt();
                }
                if (selectedID == -1) {
                    shakeUtilityPrompt();
                }
                Toast.makeText(AccountSettings.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            handleUtility();
            if (accID.length() != 9) {
                shakeAccIDField();
                accIdPrompt.setText("Invalid account ID");
                accIdPrompt.postDelayed(() -> accIdPrompt.setText("Enter utility account ID:"), 1000);
            }
            if (firstName.isBlank() && lastName.isBlank()) {
                namePrompt.setText("Name can't be blank");
                namePrompt.postDelayed(() -> namePrompt.setText("Enter name registered:"), 1000);
                shakeFirstNameField();
                shakeLastNameField();
                cont = false;
            }
            if (!isValidName(firstName) && !isValidName(lastName)) {
                namePrompt.setText("Name can't contain special characters/numbers");
                namePrompt.postDelayed(() -> namePrompt.setText("Enter name registered:"), 1000);
                shakeFirstNameField();
                shakeLastNameField();
                cont = false;
            } else if (!isValidName(firstName)) {
                namePrompt.setText("Invalid first name");
                namePrompt.postDelayed(() -> namePrompt.setText("Enter name registered:"), 1000);
                shakeFirstNameField();
                cont = false;
            } else if (!isValidName(lastName)) {
                namePrompt.setText("Invalid last name");
                namePrompt.postDelayed(() -> namePrompt.setText("Enter name registered:"), 1000);
                shakeLastNameField();
                cont = false;
            }
            if (cont) {
                Log.d("Anona", "Eto sha " + "\n" + utility + "\n" + accID + "\n" + accName);
                addUtility();
            }
        });
    }

    private void handleUtility(int utilityID){
        Intent i = new Intent(AccountSettings.this, EditUtilityAccount.class);
        i.putExtra("utilityTypeID", String.valueOf(utilityID));
        startActivity(i);
    }

    private void fetchData() {
        kuryente = false;
        internet = false;
        tubig = false;

        btnKuryente.setVisibility(View.GONE);
        btnInternet.setVisibility(View.GONE);
        btnTubig.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int numberOfChildren = (int) snapshot.child("Utilities").getChildrenCount();
                        if (numberOfChildren <= 0){
                            linkedAccTV.setText("No Linked Account");
                        }else{
                            linkedAccTV.setText("Linked Utility Accounts:");
                        }
                        if (numberOfChildren > 2){
                            btnAddUtility.setVisibility(View.GONE);
                        }else{
                            btnAddUtility.setVisibility(View.VISIBLE);
                        }

                        for (int i = 1; i <= 3; i++) {
                            String utilityType = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityType").getValue(String.class);
                            String utilityAccName = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityAccName").getValue(String.class);
                            String utilityAccID = snapshot.child("Utilities").child(String.valueOf(i)).child("utilityAccID").getValue(String.class);
                            Log.d("FirebaseData", "Utility ID: " + ", Utility Type: " + utilityType);

                            if (utilityType != null) {
                                if (utilityType.equals("Kuryente")) {
                                    btnKuryente.setVisibility(View.VISIBLE);
                                    kuryenteAccName = utilityAccName;
                                    kuryenteID = utilityAccID;
                                    kuryente = true;
                                    radio_option1.setVisibility(View.GONE);
                                } else if (utilityType.equals("Internet")) {
                                    btnInternet.setVisibility(View.VISIBLE);
                                    internetAccName = utilityAccName;
                                    internetID = utilityAccID;
                                    internet = true;
                                    radio_option2.setVisibility(View.GONE);
                                } else if (utilityType.equals("Tubig")) {
                                    btnTubig.setVisibility(View.VISIBLE);
                                    tubigAccName = utilityAccName;
                                    tubigID = utilityAccID;
                                    tubig = true;
                                    radio_option3.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        Log.e("AccountSettings", "No data found for user ID: " + userID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("AccountSettings", "Data fetch failed: " + error.getMessage());
                }
            });
        }
    }
    public void closeForm() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        accIDfield.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        rgroup.clearCheck();
        utilityForm.setVisibility(View.GONE);
    }

    private void handleUtility() {
        selectedRadio = findViewById(selectedID);
        utility = selectedRadio.getText().toString();
    }
    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    public void shakeUtilityPrompt(){
        utilityPrompt.startAnimation(shake);
        utilityPrompt.setTextColor(Color.RED);
        cont = false;
        utilityPrompt.postDelayed(() -> utilityPrompt.setTextColor(Color.GRAY), 600);
    }
    public void shakeAccIDPrompt(){
        accIdPrompt.startAnimation(shake);
        accIdPrompt.setTextColor(Color.RED);
        cont = false;
        accIdPrompt.postDelayed(() -> accIdPrompt.setTextColor(Color.GRAY), 600);
    }
    public void shakeAccIDField(){
        accIDfield.startAnimation(shake);
        accIDfield.setTextColor(Color.RED);
        cont = false;
        accIDfield.postDelayed(() -> accIDfield.setTextColor(Color.BLACK), 600);
    }
    public void shakeNamePrompt(){
        namePrompt.startAnimation(shake);
        namePrompt.setTextColor(Color.RED);
        cont = false;
        namePrompt.postDelayed(() -> namePrompt.setTextColor(Color.GRAY), 600);
    }
    public void shakeFirstNameField(){
        firstNameField.startAnimation(shake);
        firstNameField.setTextColor(Color.RED);
        cont = false;
        firstNameField.postDelayed(() -> firstNameField.setTextColor(Color.BLACK), 600);
    }
    public void shakeLastNameField(){
        lastNameField.startAnimation(shake);
        lastNameField.setTextColor(Color.RED);
        cont = false;
        lastNameField.postDelayed(() -> lastNameField.setTextColor(Color.BLACK), 600);
    }
    @Override
    public void onBackPressed() {
        if(utilityForm.getVisibility()==View.VISIBLE){
            closeForm();
        }else{
            finish();
        }
    }
    protected void onResume() {
        super.onResume();
        fetchData();
    }
}