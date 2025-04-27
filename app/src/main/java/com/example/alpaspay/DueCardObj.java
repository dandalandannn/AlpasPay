package com.example.alpaspay;

public class DueCardObj {
    public String  utilityType = "default type";
    public String  dueCardAmount = "default amount";
    public String  dueCardDueDate = "default due";

    public String balance;
    public String utilityAccID;
    public String utilityAccName;

    public DueCardObj(String utilityType, String dueCardAmount, String dueCardDueDate, String balance, String utilityAccID, String utilityAccName) {
        if (utilityType!=null){
            this.utilityType = utilityType;
        }
        if (dueCardAmount!=null){
            this.dueCardAmount = dueCardAmount;
        }
        if (dueCardDueDate!=null){
            this.dueCardDueDate = dueCardDueDate;
        }
        this.utilityAccName = utilityAccName;
        this.utilityAccID = utilityAccID;
        this.balance = balance;
    }

}