package com.example.alpaspay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HelpDesk extends AppCompatActivity {

    private TextView q1TV,q2TV,q3TV,q4TV,q5TV,q6TV,q7TV;
    private TextView a1TV,a2TV,a3TV,a4TV,a5TV,a6TV,a7TV;

    TextView[] questions;
    TextView[] answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help_desk);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        q1TV = findViewById(R.id.q1TV);
        q2TV = findViewById(R.id.q2TV);
        q3TV = findViewById(R.id.q3TV);
        q4TV = findViewById(R.id.q4TV);
        q5TV = findViewById(R.id.q5TV);
        q6TV = findViewById(R.id.q6TV);
        q7TV = findViewById(R.id.q7TV);

        a1TV = findViewById(R.id.a1TV);
        a2TV = findViewById(R.id.a2TV);
        a3TV = findViewById(R.id.a3TV);
        a4TV = findViewById(R.id.a4TV);
        a5TV = findViewById(R.id.a5TV);
        a6TV = findViewById(R.id.a6TV);
        a7TV = findViewById(R.id.a7TV);

        questions = new TextView[]{q1TV, q2TV, q3TV, q4TV, q5TV, q6TV, q7TV};
        answers = new TextView[]{a1TV, a2TV, a3TV, a4TV, a5TV, a6TV, a7TV};

        for (TextView answer : answers) {
            answer.setVisibility(View.GONE);
        }

        for (int i = 0; i < questions.length; i++) {
            final int index = i;
            questions[i].setOnClickListener(v -> {
                if (answers[index].getVisibility() == View.GONE) {
                    answers[index].setVisibility(View.VISIBLE);
                } else {
                    answers[index].setVisibility(View.GONE);
                }
            });
        }

    }
}