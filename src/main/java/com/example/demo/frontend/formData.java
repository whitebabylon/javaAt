package com.example.demo.frontend;

public class formData {
    String relationshipID;

    public String getRelationshipID() {
        return relationshipID;
    }

    public void setRelationshipID(String relationshipID) {
        this.relationshipID = relationshipID;
    }

    @Override
    public String toString() {
        return "formData{" +
                "relationshipID='" + relationshipID + '\'' +
                '}';
    }
}
