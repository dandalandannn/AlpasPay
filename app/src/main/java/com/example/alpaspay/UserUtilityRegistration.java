package com.example.alpaspay;

public class UserUtilityRegistration {
    private String utilityTypeID;
    private String utilityAccID;
    private String utilityAccName;

    public UserUtilityRegistration(String utilityTypeID, String utilityAccID, String utilityAccName) {
        this.utilityTypeID = utilityTypeID;
        this.utilityAccID = utilityAccID;
        this.utilityAccName = utilityAccName;
    }
    public String getUtilityTypeID() {
        return utilityTypeID;
    }

    public void setUtilityTypeID(String utilityAccID) {
        this.utilityTypeID = utilityTypeID;
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
}
