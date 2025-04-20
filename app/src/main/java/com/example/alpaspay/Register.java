package com.example.alpaspay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private TextView utilityPrompt, accIdPrompt, namePrompt;

    private RadioGroup rgroup;
    private int selectedID;
    private Button btnToSignup;
    private TextView btnLToLogin;
    private RadioButton selectedRadio;
    private String utility;
    private EditText accIDfield;
    private String accID;
    private String prettyAccID;
    private EditText firstNameField;
    private EditText lastNameField;
    private String firstName, lastName, accName;
    private Button btn_validID;
    private boolean cont;
    private boolean accIdOk, fNameOk, lNameOk;

    private Animation shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        utilityPrompt = findViewById(R.id.utilityPrompt);
        accIdPrompt = findViewById(R.id.accIdPrompt);
        namePrompt = findViewById(R.id.namePrompt);

        rgroup = findViewById(R.id.options);
        btnToSignup = findViewById(R.id.btn_continueToSignUp);
        btnLToLogin = findViewById(R.id.btn_backToLogin);

        accIDfield = findViewById(R.id.account_id);
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        cont = false;

        btnToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont = true;
                selectedID = rgroup.getCheckedRadioButtonId();
                accID = accIDfield.getText().toString().trim();
                firstName = firstNameField.getText().toString().trim();
                lastName = lastNameField.getText().toString().trim();
                accName = firstName + " " + lastName;
                if (accID.isEmpty() || firstName.isBlank() || lastName.isBlank() || selectedID == -1) {

                    if(accID.isEmpty()){
                        shakeAccIDPrompt();
                    }
                    if (firstName.isBlank() || lastName.isBlank()) {
                        shakeNamePrompt();
                    }
                    if (selectedID == -1){
                        shakeUtilityPrompt();
                    }
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                handleUtility();

                //utility ID
                if(accID.length() != 9){
                    shakeAccIDField();
                    accIdPrompt.setText("Invalid account ID");
                    accIdPrompt.postDelayed(() -> accIdPrompt.setText("Enter utility account ID:"),1000);
                }
                //acc name
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

                //all okay
                if (cont){
                    Log.d("Anona", "Eto sha " + "\n" + utility+ "\n" + accID+ "\n" + accName);
                    Intent i = new Intent(Register.this, Signin.class);
                    i.putExtra("utility", utility);
                    i.putExtra("accID", accID);
                    i.putExtra("accName", accName);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(Register.this, "Unable register, contact Dan.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnLToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });
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
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}