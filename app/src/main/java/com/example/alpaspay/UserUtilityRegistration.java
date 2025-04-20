package com.example.alpaspay;

public class UserUtilityRegistration {
    private String utilityType;
    private String utilityAccID;
    private String utilityAccName;

    public UserUtilityRegistration(String utilityType, String utilityAccID, String utilityAccName) {
        this.utilityType = utilityType;
        this.utilityAccID = utilityAccID;
        this.utilityAccName = utilityAccName;
    }
    public String getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(String utilityAccID) {
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
}
