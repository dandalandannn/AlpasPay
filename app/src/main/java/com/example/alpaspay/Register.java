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

    private ProgressBar progressBar;

    private RadioGroup rgroup;
    private int selectedID;
    private Button btnToSignup;
    private TextView btnLToLogin;
    private RadioButton selectedRadio;
    private String utility;
    private EditText accIDfield;
    private String accID;
    private String prettyAccID;
    private EditText accNamefield;
    private String accName;
    private Button btn_validID;
    private Boolean cont;

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
        progressBar = findViewById(R.id.progressBar);

        prettyAccID = "";
        rgroup = findViewById(R.id.options);
        btnToSignup = findViewById(R.id.btn_continueToSignUp);
        btnLToLogin = findViewById(R.id.btn_backToLogin);

        accIDfield = findViewById(R.id.account_id);
        accNamefield = findViewById(R.id.account_name);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        cont = false;

        btnToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont = true;
                accID = accIDfield.getText().toString().trim();
                accName = accNamefield.getText().toString().trim();
                if (accID.isEmpty() || accName.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedID = rgroup.getCheckedRadioButtonId();
                if (selectedID == -1) {
                    Toast.makeText(Register.this, "Please select a utility option", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedRadio = findViewById(selectedID);
                utility = selectedRadio.getText().toString();
                if(accID.length() != 9){
                    checkIDfield();
                }else{
                    for(int i = 0; i<accID.length(); i++){
                        Log.d("haha", ""+ accID.charAt(i));
                        prettyAccID += accID.charAt(i);
                        if((i+1)%3==0){
                            prettyAccID += " ";
                        }
                    }
                    accIDfield.setText(prettyAccID);
                }
                if(containsNumber(accName)){
                    accNamefield.startAnimation(shake);
                    accNamefield.setTextColor(Color.RED);
                    Toast.makeText(Register.this, "Name can't contain numbers!", Toast.LENGTH_SHORT).show();
                    cont = false;

                    accNamefield.postDelayed(() -> accNamefield.setTextColor(Color.BLACK), 600);
                }

                if (cont){
                    progressBar.setVisibility(View.VISIBLE);

                    progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 2000);
                    Intent i = new Intent(Register.this, Signin.class);
                    startActivity(i);
                    Log.d("Anona", "Eto sha " + "\n" + utility+ "\n" + accID+ "\n" + accName);
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
    public void checkIDfield(){
        accIDfield.startAnimation(shake);
        accIDfield.setTextColor(Color.RED);
        Toast.makeText(Register.this, "Invalid ID!", Toast.LENGTH_SHORT).show();
        cont = false;

        accIDfield.postDelayed(() -> accIDfield.setTextColor(Color.BLACK), 600);
    }
    public boolean containsNumber(String text) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
        @Override
    public void onBackPressed() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}