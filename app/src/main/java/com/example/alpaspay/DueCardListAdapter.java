package com.example.alpaspay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DueCardListAdapter extends ArrayAdapter<DueCardObj> {
    public DueCardListAdapter(@NonNull Context context, ArrayList<DueCardObj> dueCardObjs) {
        super(context, R.layout.due_card, dueCardObjs);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
        DueCardObj dueCardObj = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.due_card, parent,false);
        }

        TextView utilityName = view.findViewById(R.id.utilityType);
        TextView dueCardAmount = view.findViewById(R.id.dueCardAmount);
        TextView dueCardDueDate = view.findViewById(R.id.dueCardDueDate);
        Button btnDueCardPayNow = view.findViewById(R.id.btnDueCardPayNow);

        utilityName.setText(dueCardObj.utilityType);
        dueCardDueDate.setText(dueCardObj.dueCardDueDate);
        dueCardAmount.setText(dueCardObj.dueCardAmount);

        btnDueCardPayNow.setOnClickListener(v -> {
            DueCardObj clickedCard = getItem(position);

            Intent intent = new Intent(getContext(), PayNow.class);
            intent.putExtra("utilityType", clickedCard.utilityType);
            intent.putExtra("dueCardAmount", clickedCard.dueCardAmount);
            intent.putExtra("dueCardDueDate", clickedCard.dueCardDueDate);
            intent.putExtra("balance", clickedCard.balance);
            intent.putExtra("utilityAccID", clickedCard.utilityAccID);
            intent.putExtra("utilityAccName", clickedCard.utilityAccName);

            getContext().startActivity(intent);

        });

        return view;
    }
}
