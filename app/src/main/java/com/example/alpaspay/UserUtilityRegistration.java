package com.example.alpaspay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserUtilityRegistration {
    private String utilityType;
    private String utilityAccID;
    private String utilityAccName;
    private int dueDay;
    private String dueDate;

    public UserUtilityRegistration() {
    }


    public UserUtilityRegistration(String utilityType, String utilityAccID, String utilityAccName, int dueDay) {
        this.utilityType = utilityType;
        this.utilityAccName = utilityAccName;
        this.utilityAccID = utilityAccID;
        this.dueDay = dueDay;
        this.dueDate = calculateDueDate();
    }

    public String getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
    }

    public String getUtilityAccID() {
        return utilityAccID;
    }

    public void setUtilityAccID(String utilityAccID) {
        this.utilityAccID = utilityAccID;
    }

    public String getUtilityAccName() {
        return utilityAccName;
    }

    public void setUtilityAccName(String utilityAccName) {
        this.utilityAccName = utilityAccName;
    }

    public int getDueDay() {
        return dueDay;
    }

    public void setDueDay(int dueDay) {
        this.dueDay = dueDay;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String calculateDueDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate newDate = currentDate;

        if (currentDate.getDayOfMonth() > dueDay) {
            newDate = currentDate.plusMonths(1);
        }

        newDate = newDate.withDayOfMonth(dueDay);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return newDate.format(formatter);
    }
}
