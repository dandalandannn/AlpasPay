    package com.example.alpaspay;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Patterns;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.FrameLayout;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    public class Signin extends AppCompatActivity {

        private FrameLayout loading;
        private FirebaseAuth auth;
        private EditText signupEmail, signupPass, confirm;
        private Button signupBtn;
        private TextView toLogin;
        private String utilityType, utilityAccID, utilityAccName;
        String utilityTypeID;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_signin);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            loading = findViewById(R.id.loading);

            auth = FirebaseAuth.getInstance();
            signupEmail = findViewById(R.id.signup_email);
            signupPass = findViewById(R.id.signup_password);
            confirm = findViewById(R.id.confirm_signup_password);
            signupBtn = findViewById(R.id.btn_signup);
            toLogin = findViewById(R.id.tologin);

            Intent intent = getIntent();
            utilityType = intent.getStringExtra("utility");
            utilityAccID = intent.getStringExtra("accID");
            utilityAccName = intent.getStringExtra("accName");

            if(utilityType.equals("Kuryente")){
                utilityTypeID = "1";
            } else if (utilityType.equals("Internet")) {
                utilityTypeID = "2";
            } else if (utilityType.equals("Tubig")) {
                utilityTypeID = "3";
            }else {
                utilityTypeID = "-1";
            }

            //create acc
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usern = signupEmail.getText().toString().trim();
                    String pass = signupPass.getText().toString().trim();
                    String cpass = confirm.getText().toString().trim();

                    if (usern.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please fill in every field", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!Patterns.EMAIL_ADDRESS.matcher(usern).matches()) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        } else {
                            if(securedPass(pass, cpass)){
                                loading.setVisibility(View.VISIBLE);
                                auth.createUserWithEmailAndPassword(usern, pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseUser user = auth.getCurrentUser();
                                                    if (user != null) {
                                                        user.sendEmailVerification()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> verifyTask) {
                                                                        if (verifyTask.isSuccessful()) {
                                                                            createData(user);
                                                                            Toast.makeText(getApplicationContext(), "Sign up Successful. Verification email sent!", Toast.LENGTH_LONG).show();
                                                                            startActivity(new Intent(Signin.this, Login.class));
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "Sign up Successful, but failed to send verification email.", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Sign up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                loading.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        }
                    }
                }
            });


            toLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Signin.this, Login.class));
                    finish();
                }
            });

        }

        private void createData(FirebaseUser user) {
            String uid = user.getUid();
            String usern = user.getEmail();

            UserRegistration userRegistration = new UserRegistration(
                    usern
            );
            UserUtilityRegistration userUtilityRegistration = new UserUtilityRegistration(
                    utilityTypeID,
                    utilityAccID,
                    utilityAccName
            );

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference userRef = databaseRef.child(uid);
            userRef.setValue(userRegistration);
            userRef.child("Utilities").child(utilityType).setValue(userUtilityRegistration)
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to save registration data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        }

        public boolean securedPass(String pass, String cpass) {
            if (!pass.equals(cpass)) {
                confirm.setText("");
                Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if(pass.length() < 6){
                    signupPass.setText("");
                    confirm.setText("");
                    Toast.makeText(getApplicationContext(), "Passwords can't be less than 6 characters", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!pass.matches(".*[A-Za-z].*") || !pass.matches(".*\\d.*")) {
                    signupPass.setText("");
                    confirm.setText("");
                    Toast.makeText(getApplicationContext(), "Password must include letters and numbers", Toast.LENGTH_SHORT).show();
                    return false;
            }
                return true;
            }
        }
        @Override
        public void onBackPressed() {
            Intent intent = new Intent(Signin.this, Login.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Registration cancelled", Toast.LENGTH_SHORT).show();
            finish();
            super.onBackPressed();
        }

    }